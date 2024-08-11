package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId

class NotificationService(
    private val kotbot: Kotbot,
    private val chatId: Long,
) {
    suspend fun notifyAdmins(
        message: String,
    ) {
        kotbot.execute(
            SendMessage(
                chat_id = LongChatId(chatId),
                text = message,
                parse_mode = "MarkdownV2",
            )
        )
    }
}
