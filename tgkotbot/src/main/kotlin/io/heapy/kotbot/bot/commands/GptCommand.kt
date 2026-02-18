package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.DismissGptCallbackData
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.SendGptMessageCallbackData
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.dao.GptSessionDao
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown
import io.heapy.kotbot.infra.openai.GptService

class GptCommand(
    private val kotbot: Kotbot,
    private val gptService: GptService,
    private val markdown: Markdown,
    private val callbackDataService: CallbackDataService,
    private val gptSessionDao: GptSessionDao,
) : Command {
    override val name: String = "/gpt"
    override val requiredContext = listOf(
        Command.Context.GROUP_CHAT,
        Command.Context.USER_CHAT,
    )
    override val requiredAccess = Command.Access.MODERATOR

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        when (cex.currentContext) {
            Command.Context.GROUP_CHAT -> executeInGroup()
            Command.Context.USER_CHAT -> executeInUserChat()
        }
    }

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    private suspend fun executeInGroup() {
        val message = cex.message
        val text = message.textWithoutCommand
        val replyText = message.reply_to_message?.text
        // Somehow message can have thread_id, even if it's not a forum
        val threadId = message.message_thread_id?.takeIf { message.chat.is_forum == true }

        val prompt = text
            ?: replyText
            ?: run {
                log.info("No text to process")
                return
            }

        // Send "please wait" in the group, replying to the original message
        val waitMessage = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                text = markdown.escape("Processing your request, please wait..."),
                reply_parameters = ReplyParameters(
                    message_id = message.message_id,
                    allow_sending_without_reply = true,
                ),
                parse_mode = ParseMode.MarkdownV2.name,
                message_thread_id = threadId,
            )
        )

        if (waitMessage == null) {
            log.error("Failed to send wait message in group")
            return
        }

        // Create session in DB
        val sessionId = gptSessionDao.createSession(
            userId = message.from!!.id,
            groupChatId = message.chat.id,
            waitMessageId = waitMessage.message_id,
        )

        if (sessionId == null) {
            log.error("Failed to create GPT session")
            return
        }

        // Store user prompt
        gptSessionDao.addMessage(sessionId, "user", prompt)

        val response = gptService.complete(userPrompt = prompt)
        val escaped = markdown.escape(response)

        log.info("GPT response: {}", response)
        log.info("Escaped response: {}", escaped)

        // Store assistant response
        gptSessionDao.addMessage(sessionId, "assistant", response)

        // Send preview to user's private chat with Send/Dismiss buttons
        val previewMessage = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.from!!.id),
                text = escaped,
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = buildGptReplyMarkup(
                    groupChatId = message.chat.id,
                    waitMessageId = waitMessage.message_id,
                    responseText = escaped,
                    sessionId = sessionId,
                ),
            )
        )

        // Store preview message reference
        if (previewMessage != null) {
            gptSessionDao.setPreviewMessage(
                sessionId = sessionId,
                previewChatId = previewMessage.chat.id,
                previewMessageId = previewMessage.message_id,
            )
        }
    }

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    private suspend fun executeInUserChat() {
        val message = cex.message
        val text = message.textWithoutCommand
        val replyText = message.reply_to_message?.text

        val prompt = text
            ?: replyText
            ?: run {
                log.info("No text to process")
                return
            }

        // Direct GPT response in private chat
        val sentMessage = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.from!!.id),
                text = markdown.escape("Processing your request, please wait..."),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )

        val response = gptService.complete(userPrompt = prompt)
        val escaped = markdown.escape(response)

        kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(message.from!!.id),
                message_id = sentMessage?.message_id,
                text = escaped,
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )
    }

    context(_: TransactionContext)
    private suspend fun buildGptReplyMarkup(
        groupChatId: Long,
        waitMessageId: Int,
        responseText: String,
        sessionId: Long,
    ): InlineKeyboardMarkup {
        val sendCallbackData = callbackDataService.insert(
            SendGptMessageCallbackData(
                groupChatId = groupChatId,
                waitMessageId = waitMessageId,
                responseText = responseText,
                sessionId = sessionId,
            )
        ) ?: error("Failed to create send callback data")

        val dismissCallbackData = callbackDataService.insert(
            DismissGptCallbackData(
                groupChatId = groupChatId,
                waitMessageId = waitMessageId,
                sessionId = sessionId,
            )
        ) ?: error("Failed to create dismiss callback data")

        return InlineKeyboardMarkup(
            inline_keyboard = listOf(
                listOf(
                    InlineKeyboardButton(
                        text = "Send",
                        callback_data = sendCallbackData,
                    ),
                    InlineKeyboardButton(
                        text = "Dismiss",
                        callback_data = dismissCallbackData,
                    ),
                )
            )
        )
    }

    private companion object : Logger()
}