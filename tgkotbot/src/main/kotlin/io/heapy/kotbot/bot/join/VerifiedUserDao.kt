package io.heapy.kotbot.bot.join

import io.heapy.kotbot.database.enums.VerificationSource
import io.heapy.kotbot.database.tables.VerifiedUser.Companion.VERIFIED_USER
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import java.time.LocalDateTime

data class VerifiedUserData(
    val id: Long,
    val telegramId: Long,
    val source: VerificationSource,
    val verifiedAt: LocalDateTime,
    val sessionId: Long?,
)

class VerifiedUserDao {
    context(_: TransactionContext)
    suspend fun findByTelegramId(
        telegramId: Long,
    ): VerifiedUserData? = useTx {
        dslContext
            .selectFrom(VERIFIED_USER)
            .where(VERIFIED_USER.TELEGRAM_ID.eq(telegramId))
            .fetchOne()
            ?.let {
                VerifiedUserData(
                    id = it.get(VERIFIED_USER.ID)!!,
                    telegramId = it.get(VERIFIED_USER.TELEGRAM_ID)!!,
                    source = it.get(VERIFIED_USER.SOURCE)!!,
                    verifiedAt = it.get(VERIFIED_USER.VERIFIED_AT)!!,
                    sessionId = it.get(VERIFIED_USER.SESSION_ID),
                )
            }
    }

    context(_: TransactionContext)
    suspend fun insertVerified(
        telegramId: Long,
        source: VerificationSource,
        sessionId: Long? = null,
    ) = useTx {
        dslContext
            .insertInto(
                VERIFIED_USER,
                VERIFIED_USER.TELEGRAM_ID,
                VERIFIED_USER.SOURCE,
                VERIFIED_USER.VERIFIED_AT,
                VERIFIED_USER.SESSION_ID,
            )
            .values(
                telegramId,
                source,
                LocalDateTime.now(),
                sessionId,
            )
            .onConflict(VERIFIED_USER.TELEGRAM_ID)
            .doNothing()
            .execute()
    }

    context(_: TransactionContext)
    suspend fun insertExistingMember(
        telegramId: Long,
    ) = useTx {
        dslContext
            .insertInto(
                VERIFIED_USER,
                VERIFIED_USER.TELEGRAM_ID,
                VERIFIED_USER.SOURCE,
                VERIFIED_USER.VERIFIED_AT,
            )
            .values(
                telegramId,
                VerificationSource.EXISTING_MEMBER,
                LocalDateTime.now(),
            )
            .onConflict(VERIFIED_USER.TELEGRAM_ID)
            .doNothing()
            .execute()
    }
}
