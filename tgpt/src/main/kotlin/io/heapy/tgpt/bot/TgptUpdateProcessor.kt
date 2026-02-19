package io.heapy.tgpt.bot

import com.openai.models.ChatModel
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam
import com.openai.models.chat.completions.ChatCompletionContentPart
import com.openai.models.chat.completions.ChatCompletionContentPartImage
import com.openai.models.chat.completions.ChatCompletionCreateParams
import com.openai.models.chat.completions.ChatCompletionMessageParam
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam
import com.openai.models.chat.completions.ChatCompletionUserMessageParam
import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TelegramApiError
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.method.SendChecklist
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.InputChecklist
import io.heapy.kotbot.bot.model.InputChecklistTask
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.model.chatId
import io.heapy.kotbot.infra.markdown.Markdown
import io.heapy.tgpt.bot.dao.AllowedUserDao
import io.heapy.tgpt.bot.dao.ApiCallDao
import io.heapy.tgpt.bot.dao.ThreadDao
import io.heapy.tgpt.bot.dao.ThreadMessageDao
import io.heapy.tgpt.database.enums.ContentType
import io.heapy.tgpt.database.enums.MessageRole
import io.heapy.tgpt.database.tables.pojos.ThreadMessage
import io.heapy.tgpt.infra.jdbc.TransactionContext
import io.heapy.tgpt.infra.jdbc.TransactionProvider
import io.heapy.tgpt.openai.CostCalculator
import io.heapy.tgpt.openai.OpenAiService
import io.heapy.tgpt.openai.TelegramFileService
import java.util.*

class TgptUpdateProcessor(
    private val kotbot: Kotbot,
    private val openAiService: OpenAiService,
    private val telegramFileService: TelegramFileService,
    private val allowedUserDao: AllowedUserDao,
    private val threadDao: ThreadDao,
    private val threadMessageDao: ThreadMessageDao,
    private val apiCallDao: ApiCallDao,
    private val transactionProvider: TransactionProvider,
    private val markdown: Markdown,
) {
    suspend fun processUpdate(update: TypedUpdate) {
        when (update) {
            is TypedMessage -> processMessage(update.value)
            is TypedEditedMessage -> {} // ignore edits
        }
    }

    private suspend fun processMessage(message: Message) {
        val from = message.from ?: return
        val chatId = message.chat.id
        val userId = from.id

        if (processPublicCommand(message)) {
            return
        }

        // Check if user is allowed
        val isAllowed = transactionProvider.transaction {
            allowedUserDao.isAllowed(userId)
        }
        if (!isAllowed) {
            log.debug("User $userId is not in the allowed list, ignoring message")
            return
        }

        if (processCommand(message)) {
            return
        }

        // Extract content
        val extracted = extractContent(message) ?: return

        // Determine thread: new, continue, or fork
        val replyToMessageId = message.reply_to_message?.message_id

        val (threadId, isNewThread) = transactionProvider.transaction {
            resolveThread(chatId, userId, replyToMessageId)
        }

        // Add system prompt for new threads
        if (isNewThread) {
            transactionProvider.transaction {
                threadMessageDao.addMessage(
                    threadId = threadId,
                    role = MessageRole.system,
                    contentType = ContentType.text,
                    content = ensureTelegramMarkdownPrompt(openAiService.systemPrompt),
                )
            }
        }

        // Add user message
        transactionProvider.transaction {
            threadMessageDao.addMessage(
                threadId = threadId,
                role = MessageRole.user,
                contentType = extracted.contentType,
                content = extracted.content,
                telegramMessageId = message.message_id,
                telegramUserId = userId,
            )
        }

        // Build OpenAI messages and call API
        val threadMessages = transactionProvider.transaction {
            threadMessageDao.getThreadMessages(threadId)
        }

        val openAiMessages = prependTelegramMarkdownPrompt(
            buildOpenAiMessages(threadMessages),
        )

        val params = ChatCompletionCreateParams.builder()
            .model(ChatModel.of(openAiService.model()))
            .messages(openAiMessages)
            .maxCompletionTokens(openAiService.maxTokens().toLong())
            .build()

        val completion = openAiService.chatCompletion(params)

        val responseText = completion.choices().firstOrNull()
            ?.message()?.content()?.orElse(null)
            ?: "No response from the model."

        // Send reply to Telegram
        val sentMessage = replyToMessage(
            message = message,
            text = responseText,
        )

        // Store assistant message
        transactionProvider.transaction {
            threadMessageDao.addMessage(
                threadId = threadId,
                role = MessageRole.assistant,
                contentType = ContentType.text,
                content = responseText,
                telegramMessageId = sentMessage.message_id,
            )
        }

        // Record API call with cost
        val usage = completion.usage()
        if (usage.isPresent) {
            val usageVal = usage.get()
            val promptTokens = usageVal.promptTokens().toInt()
            val completionTokens = usageVal.completionTokens().toInt()
            val totalTokens = usageVal.totalTokens().toInt()
            val cost = CostCalculator.estimateCost(
                model = openAiService.model(),
                promptTokens = promptTokens,
                completionTokens = completionTokens,
            )
            transactionProvider.transaction {
                apiCallDao.recordApiCall(
                    threadId = threadId,
                    telegramUserId = userId,
                    model = openAiService.model(),
                    promptTokens = promptTokens,
                    completionTokens = completionTokens,
                    totalTokens = totalTokens,
                    estimatedCostUsd = cost,
                )
            }
        }
    }

    context(tx: TransactionContext)
    private suspend fun resolveThread(
        chatId: Long,
        userId: Long,
        replyToMessageId: Int?,
    ): Pair<Long, Boolean> {
        if (replyToMessageId == null) {
            val threadId = threadDao.createThread(chatId = chatId, createdBy = userId)
            return threadId to true
        }

        val existingThreadId = threadDao.findThreadByTelegramMessageId(replyToMessageId)

        if (existingThreadId == null) {
            val threadId = threadDao.createThread(chatId = chatId, createdBy = userId)
            return threadId to true
        }

        val latestAssistantMsgId = threadMessageDao.getLatestAssistantMessageId(existingThreadId)

        return if (latestAssistantMsgId == replyToMessageId) {
            existingThreadId to false
        } else {
            val messagesUpTo = threadMessageDao.getMessagesUpTo(existingThreadId, replyToMessageId)
            val newThreadId = threadDao.createThread(
                chatId = chatId,
                createdBy = userId,
                forkedFromThreadId = existingThreadId,
                forkedAtMessageId = replyToMessageId.toLong(),
            )
            threadMessageDao.copyMessagesToThread(messagesUpTo, newThreadId)
            newThreadId to false
        }
    }

    private data class ExtractedContent(
        val content: String,
        val contentType: ContentType,
    )

    private data class ParsedCommand(
        val name: String,
        val argument: String?,
    )

    private suspend fun processPublicCommand(message: Message): Boolean {
        val text = message.text ?: return false
        val command = parseCommand(text) ?: return false

        return when (command.name) {
            "me" -> {
                replyToMessage(
                    message = message,
                    text = buildMeInfo(message),
                )
                true
            }

            else -> false
        }
    }

    private suspend fun processCommand(message: Message): Boolean {
        val text = message.text ?: return false
        val command = parseCommand(text) ?: return false

        return when (command.name) {
            "checklist" -> {
                processChecklistCommand(
                    message = message,
                    argument = command.argument,
                )
                true
            }

            else -> false
        }
    }

    private suspend fun processChecklistCommand(
        message: Message,
        argument: String?,
    ) {
        val sourceText = argument?.takeIf { it.isNotBlank() }
            ?: message.reply_to_message?.text
            ?: message.reply_to_message?.caption

        if (sourceText.isNullOrBlank()) {
            replyToMessage(
                message = message,
                text = "Usage: /checklist <text>\nOr reply to a text message with /checklist.",
            )
            return
        }

        val params = ChatCompletionCreateParams.builder()
            .model(ChatModel.of(openAiService.model()))
            .messages(
                listOf(
                    ChatCompletionMessageParam.ofSystem(
                        ChatCompletionSystemMessageParam.builder()
                            .content(CHECKLIST_SYSTEM_PROMPT)
                            .build()
                    ),
                    ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam.builder()
                            .content(sourceText)
                            .build()
                    ),
                )
            )
            .maxCompletionTokens(openAiService.maxTokens().toLong())
            .build()

        val completion = openAiService.chatCompletion(params)
        val checklistText = completion.choices().firstOrNull()
            ?.message()?.content()?.orElse(null)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: "Could not generate checklist."
        val checklistTasks = parseChecklistTasks(checklistText)

        if (checklistTasks.isEmpty()) {
            replyToMessage(
                message = message,
                text = "Could not generate checklist.",
            )
            return
        }

        val businessConnectionId = message.business_connection_id
        if (businessConnectionId != null) {
            try {
                kotbot.execute(
                    SendChecklist(
                        business_connection_id = businessConnectionId,
                        chat_id = message.chat.id,
                        checklist = InputChecklist(
                            title = CHECKLIST_DEFAULT_TITLE,
                            tasks = checklistTasks.mapIndexed { index, task ->
                                InputChecklistTask(
                                    id = index + 1,
                                    text = task,
                                )
                            },
                            others_can_add_tasks = true,
                            others_can_mark_tasks_as_done = true,
                        ),
                        reply_parameters = ReplyParameters(
                            message_id = message.message_id,
                        ),
                    )
                )
                return
            } catch (error: Exception) {
                log.warn("sendChecklist failed, falling back to text checklist", error)
            }
        }

        val textFallback = checklistTasks.joinToString("\n") { "- [ ] $it" }
        val responseText = if (businessConnectionId == null) {
            "$CHECKLIST_TEXT_FALLBACK_NOTE\n\n$textFallback"
        } else {
            textFallback
        }

        replyToMessage(
            message = message,
            text = responseText,
        )
    }

    private fun parseCommand(text: String): ParsedCommand? {
        val trimmed = text.trim()
        if (!trimmed.startsWith("/")) {
            return null
        }

        val separatorIndex = trimmed.indexOfAny(COMMAND_SEPARATOR_CHARS)
        val commandToken = if (separatorIndex == -1) {
            trimmed
        } else {
            trimmed.substring(0, separatorIndex)
        }

        val commandName = commandToken.removePrefix("/")
            .substringBefore("@")
            .lowercase()
            .trim()
        if (commandName.isBlank()) {
            return null
        }

        val argument = if (separatorIndex == -1) {
            null
        } else {
            trimmed.substring(separatorIndex).trim().ifBlank { null }
        }

        return ParsedCommand(
            name = commandName,
            argument = argument,
        )
    }

    private fun buildMeInfo(message: Message): String {
        val user = message.from ?: return "No user info available."
        val fullName = listOfNotNull(user.first_name, user.last_name)
            .joinToString(" ")
            .ifBlank { "-" }
        val username = user.username?.let { "@$it" } ?: "-"
        val language = user.language_code ?: "-"
        val premium = user.is_premium?.toString() ?: "-"
        val chat = message.chat
        val chatTitle = chat.title
            ?: listOfNotNull(chat.first_name, chat.last_name).joinToString(" ").ifBlank { null }
            ?: chat.username?.let { "@$it" }
            ?: "-"
        val chatUsername = chat.username?.let { "@$it" } ?: "-"

        return """
            |User
            |id: ${user.id}
            |name: $fullName
            |username: $username
            |language: $language
            |premium: $premium
            |
            |Chat
            |id: ${chat.id}
            |type: ${chat.type}
            |title: $chatTitle
            |username: $chatUsername
        """.trimMargin()
    }

    private suspend fun replyToMessage(
        message: Message,
        text: String,
    ): Message {
        val escapedText = markdown.escape(text)
        return try {
            kotbot.execute(
                SendMessage(
                    chat_id = message.chat.id.chatId,
                    text = escapedText,
                    parse_mode = ParseMode.MarkdownV2.name,
                    reply_parameters = ReplyParameters(
                        message_id = message.message_id,
                    ),
                )
            )
        } catch (error: TelegramApiError) {
            val description = error.message.orEmpty()
            if (!description.contains("can't parse entities", ignoreCase = true)) {
                throw error
            }
            log.warn(
                "MarkdownV2 rendering failed, sending plain text fallback. Original preview='{}', escaped preview='{}'",
                text.take(LOG_PREVIEW_LENGTH),
                escapedText.take(LOG_PREVIEW_LENGTH),
                error,
            )
            kotbot.execute(
                SendMessage(
                    chat_id = message.chat.id.chatId,
                    text = text,
                    reply_parameters = ReplyParameters(
                        message_id = message.message_id,
                    ),
                )
            )
        }
    }

    private fun ensureTelegramMarkdownPrompt(prompt: String): String {
        if (prompt.contains("MarkdownV2", ignoreCase = true)) {
            return prompt
        }
        return "$prompt\n\n$TELEGRAM_MARKDOWN_SYSTEM_PROMPT"
    }

    private fun prependTelegramMarkdownPrompt(
        messages: List<ChatCompletionMessageParam>,
    ): List<ChatCompletionMessageParam> {
        val markdownPrompt = ChatCompletionMessageParam.ofSystem(
            ChatCompletionSystemMessageParam.builder()
                .content(TELEGRAM_MARKDOWN_SYSTEM_PROMPT)
                .build()
        )
        return listOf(markdownPrompt) + messages
    }

    private fun parseChecklistTasks(text: String): List<String> {
        return text
            .lineSequence()
            .map { it.trim() }
            .mapNotNull { rawLine ->
                if (rawLine.isBlank()) {
                    return@mapNotNull null
                }

                val unprefixed = rawLine
                    .replace(CHECKLIST_MARK_PREFIX_REGEX, "")
                    .replace(BULLET_PREFIX_REGEX, "")
                    .trim()
                if (unprefixed.isBlank()) {
                    return@mapNotNull null
                }

                unprefixed.take(CHECKLIST_TASK_MAX_LENGTH)
            }
            .take(CHECKLIST_MAX_TASKS)
            .toList()
    }

    private suspend fun extractContent(message: Message): ExtractedContent? {
        // Photo
        val photos = message.photo
        if (!photos.isNullOrEmpty()) {
            val largest = photos.maxBy { it.file_size ?: 0 }
            return ExtractedContent(
                content = largest.file_id,
                contentType = ContentType.image_url,
            )
        }

        // Voice
        val voice = message.voice
        if (voice != null) {
            val audioBytes = telegramFileService.downloadFile(voice.file_id)
            val transcription = openAiService.transcribe(audioBytes)
            return ExtractedContent(
                content = transcription,
                contentType = ContentType.transcription,
            )
        }

        // Video note
        val videoNote = message.video_note
        if (videoNote != null) {
            val audioBytes = telegramFileService.downloadFile(videoNote.file_id)
            val transcription = openAiService.transcribe(audioBytes)
            return ExtractedContent(
                content = transcription,
                contentType = ContentType.transcription,
            )
        }

        // Text
        val text = message.text
        if (text != null) {
            return ExtractedContent(
                content = text,
                contentType = ContentType.text,
            )
        }

        // Caption (for photos with text)
        val caption = message.caption
        if (caption != null) {
            return ExtractedContent(
                content = caption,
                contentType = ContentType.text,
            )
        }

        return null
    }

    private suspend fun buildOpenAiMessages(
        threadMessages: List<ThreadMessage>,
    ): List<ChatCompletionMessageParam> {
        return threadMessages.map { msg ->
            when (msg.role) {
                MessageRole.system -> ChatCompletionMessageParam.ofSystem(
                    ChatCompletionSystemMessageParam.builder()
                        .content(msg.content)
                        .build()
                )

                MessageRole.user -> when (msg.contentType) {
                    ContentType.image_url -> {
                        val imageBytes = telegramFileService.downloadFile(msg.content)
                        val base64 = Base64.getEncoder().encodeToString(imageBytes)
                        val imageUrl = "data:image/jpeg;base64,$base64"

                        ChatCompletionMessageParam.ofUser(
                            ChatCompletionUserMessageParam.builder()
                                .contentOfArrayOfContentParts(
                                    listOf(
                                        ChatCompletionContentPart.ofImageUrl(
                                            ChatCompletionContentPartImage.builder()
                                                .imageUrl(
                                                    ChatCompletionContentPartImage.ImageUrl.builder()
                                                        .url(imageUrl)
                                                        .build()
                                                )
                                                .build()
                                        ),
                                    )
                                )
                                .build()
                        )
                    }

                    ContentType.transcription -> ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam.builder()
                            .content("[Voice message]: ${msg.content}")
                            .build()
                    )

                    ContentType.text -> ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam.builder()
                            .content(msg.content)
                            .build()
                    )

                    null -> ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam.builder()
                            .content(msg.content)
                            .build()
                    )
                }

                MessageRole.assistant -> ChatCompletionMessageParam.ofAssistant(
                    ChatCompletionAssistantMessageParam.builder()
                        .content(msg.content)
                        .build()
                )
            }
        }
    }

    private companion object : Logger() {
        private val COMMAND_SEPARATOR_CHARS = charArrayOf(' ', '\n', '\t', '\r')
        private val CHECKLIST_MARK_PREFIX_REGEX = Regex("""^[-*]?\s*\[(?:\s|x|X)\]\s*""")
        private val BULLET_PREFIX_REGEX = Regex("""^[-*]\s*""")
        private const val CHECKLIST_MAX_TASKS = 30
        private const val CHECKLIST_TASK_MAX_LENGTH = 100
        private const val CHECKLIST_DEFAULT_TITLE = "Checklist"
        private const val CHECKLIST_TEXT_FALLBACK_NOTE =
            "Interactive Telegram checklist requires a connected Telegram Business account. Sending text checklist."
        private const val TELEGRAM_MARKDOWN_SYSTEM_PROMPT =
            "Generate a response that would work well with MarkdownV2 format in Telegram."
        private const val LOG_PREVIEW_LENGTH = 500
        private val CHECKLIST_SYSTEM_PROMPT = """
            You convert user text into a Telegram checklist.
            Output rules:
            - Return plain text only.
            - Every line must be one checklist item and start with "- [ ] ".
            - Keep items concise and actionable.
            - Preserve the intent and order of the input.
            - Do not include any intro, explanation, or extra formatting.
        """.trimIndent()
    }
}
