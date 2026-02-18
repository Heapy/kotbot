package io.heapy.tgpt.bot

import com.openai.models.ChatModel
import com.openai.models.chat.completions.ChatCompletionContentPart
import com.openai.models.chat.completions.ChatCompletionContentPartImage
import com.openai.models.chat.completions.ChatCompletionCreateParams
import com.openai.models.chat.completions.ChatCompletionMessageParam
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam
import com.openai.models.chat.completions.ChatCompletionUserMessageParam
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam
import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.model.chatId
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
import java.util.Base64

class TgptUpdateProcessor(
    private val kotbot: Kotbot,
    private val openAiService: OpenAiService,
    private val telegramFileService: TelegramFileService,
    private val allowedUserDao: AllowedUserDao,
    private val threadDao: ThreadDao,
    private val threadMessageDao: ThreadMessageDao,
    private val apiCallDao: ApiCallDao,
    private val transactionProvider: TransactionProvider,
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

        // Check if user is allowed
        val isAllowed = transactionProvider.transaction {
            allowedUserDao.isAllowed(userId)
        }
        if (!isAllowed) {
            log.debug("User $userId is not in the allowed list, ignoring message")
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
                    content = openAiService.systemPrompt,
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

        val openAiMessages = buildOpenAiMessages(threadMessages)

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
        val sentMessage = kotbot.execute(
            SendMessage(
                chat_id = chatId.chatId,
                text = responseText,
                reply_parameters = ReplyParameters(
                    message_id = message.message_id,
                ),
            )
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

    private suspend fun extractContent(message: Message): ExtractedContent? {
        // Photo
        val photos = message.photo
        if (photos != null && photos.isNotEmpty()) {
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

    private companion object : Logger()
}
