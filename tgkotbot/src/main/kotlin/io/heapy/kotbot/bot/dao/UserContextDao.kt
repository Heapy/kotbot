package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.enums.TelegramUserRole
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.database.tables.references.TELEGRAM_USER
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import java.time.LocalDateTime

data class UserContext(
    val internalId: Long,
    val telegramId: Long,
    val created: LocalDateTime,
    val role: TelegramUserRole,
    val status: TelegramUserStatus,
    val lastMessage: LocalDateTime,
    val messageCount: Int,
    val version: Int,
)

class UserContextDao {
    context(_: TransactionContext)
    suspend fun get(
        telegramId: Long,
    ): UserContext? = useTx {
        selectFrom(TELEGRAM_USER)
            .where(TELEGRAM_USER.TELEGRAM_ID.eq(telegramId))
            .fetchOne()
            ?.let {
                UserContext(
                    internalId = it.internalId ?: error("internalId is null"),
                    telegramId = it.telegramId,
                    created = it.created ?: error("created is null"),
                    role = it.role ?: error("role is null"),
                    status = it.status ?: error("status is null"),
                    lastMessage = it.lastMessage ?: error("lastMessage is null"),
                    messageCount = it.messageCount ?: error("messageCount is null"),
                    version = it.version ?: error("version is null"),
                )
            }
    }

    context(_: TransactionContext)
    suspend fun update(
        userContext: UserContext,
    ) = useTx {
        update(TELEGRAM_USER)
            .set(TELEGRAM_USER.LAST_MESSAGE, userContext.lastMessage)
            .set(TELEGRAM_USER.MESSAGE_COUNT, userContext.messageCount)
            .set(TELEGRAM_USER.VERSION, userContext.version + 1)
            .where(
                TELEGRAM_USER.INTERNAL_ID.eq(userContext.internalId),
                TELEGRAM_USER.VERSION.eq(userContext.version),
            )
            .execute()
    }
}
