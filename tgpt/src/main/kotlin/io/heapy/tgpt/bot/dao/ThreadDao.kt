package io.heapy.tgpt.bot.dao

import io.heapy.tgpt.database.tables.Thread.Companion.THREAD
import io.heapy.tgpt.database.tables.ThreadMessage.Companion.THREAD_MESSAGE
import io.heapy.tgpt.infra.jdbc.TransactionContext
import io.heapy.tgpt.infra.jdbc.useTx

class ThreadDao {
    context(tx: TransactionContext)
    suspend fun createThread(
        chatId: Long,
        createdBy: Long,
        forkedFromThreadId: Long? = null,
        forkedAtMessageId: Long? = null,
    ): Long = useTx {
        dslContext.insertInto(THREAD)
            .set(THREAD.CHAT_ID, chatId)
            .set(THREAD.CREATED_BY, createdBy)
            .apply {
                if (forkedFromThreadId != null) set(THREAD.FORKED_FROM_THREAD_ID, forkedFromThreadId)
                if (forkedAtMessageId != null) set(THREAD.FORKED_AT_MESSAGE_ID, forkedAtMessageId)
            }
            .returning(THREAD.ID)
            .fetchOne()!!
            .id!!
    }

    context(tx: TransactionContext)
    suspend fun findThreadByTelegramMessageId(telegramMessageId: Int): Long? = useTx {
        dslContext.select(THREAD_MESSAGE.THREAD_ID)
            .from(THREAD_MESSAGE)
            .where(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID.eq(telegramMessageId))
            .fetchOne()
            ?.get(THREAD_MESSAGE.THREAD_ID)
    }

    context(tx: TransactionContext)
    suspend fun countThreads(): Int = useTx {
        dslContext.fetchCount(THREAD)
    }
}
