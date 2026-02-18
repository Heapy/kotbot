package io.heapy.kotbot.bot

import io.heapy.kotbot.infra.markdown.Markdown
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode

class NotificationService(
    private val kotbot: Kotbot,
    private val chatId: Long,
    private val markdown: Markdown,
) {
    suspend fun notifyAdmins(
        message: String,
    ) {
        kotbot.execute(
            SendMessage(
                chat_id = LongChatId(chatId),
                text = markdown.escape(message),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )
    }
}
