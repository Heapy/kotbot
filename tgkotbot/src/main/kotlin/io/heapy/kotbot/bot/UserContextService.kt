package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.database.enums.TelegramUserRole
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.infra.jdbc.TransactionContext

class UserContextService(
    private val notificationService: NotificationService,
    private val userContextDao: UserContextDao,
) {
    context(_: TransactionContext)
    suspend fun ban(
        message: Message,
        reason: String,
    ) {
        notificationService
            .notifyAdmins(
                "User ${message.from?.refLog} has been banned for $reason in chat ${message.chat.title}"
            )

        message.from?.id?.let { telegramId ->
            userContextDao.get(telegramId)?.let { userContext ->
                userContextDao.updateStatus(userContext, TelegramUserStatus.BANNED)
            }
        }
    }

    context(_: TransactionContext)
    suspend fun addStrike(
        message: Message,
        reason: String,
    ) {
        notificationService
            .notifyAdmins(
                "User ${message.from?.refLog} has been struck for $reason in chat ${message.chat.title}"
            )
    }

    context(_: TransactionContext)
    suspend fun userAccess(
        id: Long,
    ): Command.Access {
        val userContext = userContextDao.get(id) ?: return Command.Access.USER
        return when (userContext.role) {
            TelegramUserRole.ADMIN -> Command.Access.ADMIN
            TelegramUserRole.MODERATOR -> Command.Access.MODERATOR
            TelegramUserRole.USER -> Command.Access.USER
        }
    }
}
