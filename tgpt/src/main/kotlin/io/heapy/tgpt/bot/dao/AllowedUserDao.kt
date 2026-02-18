package io.heapy.tgpt.bot.dao

import io.heapy.tgpt.database.tables.AllowedUser.Companion.ALLOWED_USER
import io.heapy.tgpt.database.tables.pojos.AllowedUser
import io.heapy.tgpt.infra.jdbc.TransactionContext
import io.heapy.tgpt.infra.jdbc.useTx

class AllowedUserDao {
    context(tx: TransactionContext)
    suspend fun isAllowed(telegramId: Long): Boolean = useTx {
        dslContext.fetchExists(
            dslContext.selectFrom(ALLOWED_USER)
                .where(ALLOWED_USER.TELEGRAM_ID.eq(telegramId))
                .and(ALLOWED_USER.IS_ACTIVE.isTrue)
        )
    }

    context(tx: TransactionContext)
    suspend fun findByTelegramId(telegramId: Long): AllowedUser? = useTx {
        dslContext.selectFrom(ALLOWED_USER)
            .where(ALLOWED_USER.TELEGRAM_ID.eq(telegramId))
            .fetchOneInto(AllowedUser::class.java)
    }

    context(tx: TransactionContext)
    suspend fun addUser(telegramId: Long, username: String?, displayName: String?): AllowedUser = useTx {
        dslContext.insertInto(ALLOWED_USER)
            .set(ALLOWED_USER.TELEGRAM_ID, telegramId)
            .set(ALLOWED_USER.USERNAME, username)
            .set(ALLOWED_USER.DISPLAY_NAME, displayName)
            .returning()
            .fetchOneInto(AllowedUser::class.java)!!
    }

    context(tx: TransactionContext)
    suspend fun deactivateUser(id: Long) = useTx {
        dslContext.update(ALLOWED_USER)
            .set(ALLOWED_USER.IS_ACTIVE, false)
            .where(ALLOWED_USER.ID.eq(id))
            .execute()
    }

    context(tx: TransactionContext)
    suspend fun listAll(limit: Int, offset: Int): List<AllowedUser> = useTx {
        dslContext.selectFrom(ALLOWED_USER)
            .orderBy(ALLOWED_USER.CREATED.desc())
            .limit(limit)
            .offset(offset)
            .fetchInto(AllowedUser::class.java)
    }

    context(tx: TransactionContext)
    suspend fun countAll(): Int = useTx {
        dslContext.fetchCount(ALLOWED_USER)
    }
}
