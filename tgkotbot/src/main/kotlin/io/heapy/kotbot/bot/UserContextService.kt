package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.Message

class UserContextService(
    private val notificationService: NotificationService,
) {
    suspend fun ban(
        message: Message,
        reason: String,
    ) {
        notificationService
            .notifyAdmins(
                "User ${message.from?.info} has been banned for $reason in chat ${message.chat.title}"
            )
        // TODO: Save to database
    }

    suspend fun addStrike(
        message: Message,
        reason: String,
    ) {
        notificationService
            .notifyAdmins(
                "User ${message.from?.info} has been struck for $reason in chat ${message.chat.title}"
            )
        // TODO: Save to database
    }
}
