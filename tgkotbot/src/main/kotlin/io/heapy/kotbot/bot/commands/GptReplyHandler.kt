package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.DismissGptCallbackData
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.SendGptMessageCallbackData
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.dao.GptSessionDao
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown
import io.heapy.kotbot.infra.openai.GptService

class GptReplyHandler(
    private val kotbot: Kotbot,
    private val gptService: GptService,
    private val markdown: Markdown,
    private val callbackDataService: CallbackDataService,
    private val gptSessionDao: GptSessionDao,
) {
    /**
     * Handle a message in private chat that might be a reply to a GPT preview.
     * Returns true if the message was handled as a GPT refinement.
     */
    context(_: TransactionContext)
    suspend fun handleIfGptReply(message: Message): Boolean {
        if (message.chat.type != "private") return false

        val replyToMessage = message.reply_to_message ?: return false
        val prompt = message.text ?: return false

        val session = gptSessionDao.findByPreviewMessage(
            previewChatId = message.chat.id,
            previewMessageId = replyToMessage.message_id,
        ) ?: return false

        // Show processing state
        kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(message.chat.id),
                message_id = replyToMessage.message_id,
                text = markdown.escape("Processing your refinement, please wait..."),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )

        // Store user prompt
        gptSessionDao.addMessage(session.sessionId, "user", prompt)

        val response = gptService.complete(userPrompt = prompt)
        val escaped = markdown.escape(response)

        log.info("GPT refinement response: {}", response)

        // Store assistant response
        gptSessionDao.addMessage(session.sessionId, "assistant", response)

        // Build new callback data
        val sendCallbackData = callbackDataService.insert(
            SendGptMessageCallbackData(
                groupChatId = session.groupChatId,
                waitMessageId = session.waitMessageId,
                responseText = escaped,
                sessionId = session.sessionId,
            )
        ) ?: error("Failed to create send callback data")

        val dismissCallbackData = callbackDataService.insert(
            DismissGptCallbackData(
                groupChatId = session.groupChatId,
                waitMessageId = session.waitMessageId,
                sessionId = session.sessionId,
            )
        ) ?: error("Failed to create dismiss callback data")

        // Edit the preview message with new response and buttons
        kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(message.chat.id),
                message_id = replyToMessage.message_id,
                text = escaped,
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = InlineKeyboardMarkup(
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
                ),
            )
        )

        return true
    }

    private companion object : Logger()
}