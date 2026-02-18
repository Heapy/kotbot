package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.enums.TelegramUserRole
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.database.tables.records.TelegramUserRecord
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
    val displayName: String? = null,
)

private fun TelegramUserRecord.toUserContext(): UserContext = UserContext(
    internalId = internalId ?: error("internalId is null"),
    telegramId = telegramId,
    created = created ?: error("created is null"),
    role = role ?: error("role is null"),
    status = status ?: error("status is null"),
    lastMessage = lastMessage ?: error("lastMessage is null"),
    messageCount = messageCount ?: error("messageCount is null"),
    version = version ?: error("version is null"),
    displayName = displayName,
)

class UserContextDao {
    context(_: TransactionContext)
    suspend fun get(
        telegramId: Long,
    ): UserContext? = useTx {
        dslContext
            .selectFrom(TELEGRAM_USER)
            .where(TELEGRAM_USER.TELEGRAM_ID.eq(telegramId))
            .fetchOne()
            ?.toUserContext()
    }

    context(_: TransactionContext)
    suspend fun update(
        userContext: UserContext,
    ) = useTx {
        dslContext
            .update(TELEGRAM_USER)
            .set(TELEGRAM_USER.LAST_MESSAGE, userContext.lastMessage)
            .set(TELEGRAM_USER.MESSAGE_COUNT, userContext.messageCount)
            .set(TELEGRAM_USER.VERSION, userContext.version + 1)
            .where(
                TELEGRAM_USER.INTERNAL_ID.eq(userContext.internalId),
                TELEGRAM_USER.VERSION.eq(userContext.version),
            )
            .execute()
    }

    context(_: TransactionContext)
    suspend fun updateStatus(
        userContext: UserContext,
        status: TelegramUserStatus,
    ) = useTx {
        dslContext
            .update(TELEGRAM_USER)
            .set(TELEGRAM_USER.STATUS, status)
            .set(TELEGRAM_USER.VERSION, userContext.version + 1)
            .where(
                TELEGRAM_USER.INTERNAL_ID.eq(userContext.internalId),
                TELEGRAM_USER.VERSION.eq(userContext.version),
            )
            .execute()
    }

    context(_: TransactionContext)
    suspend fun insertIfNotExists(
        telegramId: Long,
        displayName: String?,
    ) = useTx {
        dslContext
            .insertInto(TELEGRAM_USER)
            .set(TELEGRAM_USER.TELEGRAM_ID, telegramId)
            .set(TELEGRAM_USER.DISPLAY_NAME, displayName)
            .onConflict(TELEGRAM_USER.TELEGRAM_ID)
            .doUpdate()
            .set(TELEGRAM_USER.DISPLAY_NAME, displayName)
            .execute()
    }

    context(_: TransactionContext)
    suspend fun listAll(
        limit: Int,
        offset: Int,
    ): List<UserContext> = useTx {
        dslContext
            .selectFrom(TELEGRAM_USER)
            .orderBy(TELEGRAM_USER.INTERNAL_ID)
            .limit(limit)
            .offset(offset)
            .fetch()
            .map { it.toUserContext() }
    }

    context(_: TransactionContext)
    suspend fun countAll(): Int = useTx {
        dslContext
            .selectCount()
            .from(TELEGRAM_USER)
            .fetchOne(0, Int::class.java) ?: 0
    }

    context(_: TransactionContext)
    suspend fun updateRole(
        userContext: UserContext,
        role: TelegramUserRole,
    ) = useTx {
        dslContext
            .update(TELEGRAM_USER)
            .set(TELEGRAM_USER.ROLE, role)
            .set(TELEGRAM_USER.VERSION, userContext.version + 1)
            .where(
                TELEGRAM_USER.INTERNAL_ID.eq(userContext.internalId),
                TELEGRAM_USER.VERSION.eq(userContext.version),
            )
            .execute()
    }

    context(_: TransactionContext)
    suspend fun getByInternalId(
        internalId: Long,
    ): UserContext? = useTx {
        dslContext
            .selectFrom(TELEGRAM_USER)
            .where(TELEGRAM_USER.INTERNAL_ID.eq(internalId))
            .fetchOne()
            ?.toUserContext()
    }

    context(_: TransactionContext)
    suspend fun backfillStats(
        internalId: Long,
        created: LocalDateTime,
        messageCount: Int,
    ) = useTx {
        dslContext
            .update(TELEGRAM_USER)
            .set(TELEGRAM_USER.CREATED, created)
            .set(TELEGRAM_USER.MESSAGE_COUNT, messageCount)
            .where(TELEGRAM_USER.INTERNAL_ID.eq(internalId))
            .execute()
    }
}
