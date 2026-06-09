package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.infra.markdown.Markdown

class NotificationService(
    private val kotbot: Kotbot,
    private val chatId: Long,
    private val markdown: Markdown,
) {
    suspend fun notifyAdmins(
        message: String,
    ) {
        val _ = kotbot.execute(
            SendMessage(
                chat_id = LongChatId(chatId),
                text = markdown.escape(message),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )
    }

    suspend fun notifyAdmins(
        message: String,
        replyMarkup: InlineKeyboardMarkup?,
    ): Message? =
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(chatId),
                text = markdown.escape(message),
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = replyMarkup,
            )
        )
}
