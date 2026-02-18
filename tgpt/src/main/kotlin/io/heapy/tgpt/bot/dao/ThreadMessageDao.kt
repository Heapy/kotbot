package io.heapy.tgpt.bot.dao

import io.heapy.tgpt.database.enums.ContentType
import io.heapy.tgpt.database.enums.MessageRole
import io.heapy.tgpt.database.tables.ThreadMessage.Companion.THREAD_MESSAGE
import io.heapy.tgpt.database.tables.pojos.ThreadMessage
import io.heapy.tgpt.infra.jdbc.TransactionContext
import io.heapy.tgpt.infra.jdbc.useTx

class ThreadMessageDao {
    context(tx: TransactionContext)
    suspend fun addMessage(
        threadId: Long,
        role: MessageRole,
        contentType: ContentType,
        content: String,
        telegramMessageId: Int? = null,
        telegramUserId: Long? = null,
    ): Long = useTx {
        dslContext.insertInto(THREAD_MESSAGE)
            .set(THREAD_MESSAGE.THREAD_ID, threadId)
            .set(THREAD_MESSAGE.ROLE, role)
            .set(THREAD_MESSAGE.CONTENT_TYPE, contentType)
            .set(THREAD_MESSAGE.CONTENT, content)
            .apply {
                if (telegramMessageId != null) set(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID, telegramMessageId)
                if (telegramUserId != null) set(THREAD_MESSAGE.TELEGRAM_USER_ID, telegramUserId)
            }
            .returning(THREAD_MESSAGE.ID)
            .fetchOne()!!
            .id!!
    }

    context(tx: TransactionContext)
    suspend fun getThreadMessages(threadId: Long): List<ThreadMessage> = useTx {
        dslContext.selectFrom(THREAD_MESSAGE)
            .where(THREAD_MESSAGE.THREAD_ID.eq(threadId))
            .orderBy(THREAD_MESSAGE.CREATED.asc())
            .fetchInto(ThreadMessage::class.java)
    }

    context(tx: TransactionContext)
    suspend fun getLatestAssistantMessageId(threadId: Long): Int? = useTx {
        dslContext.select(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID)
            .from(THREAD_MESSAGE)
            .where(THREAD_MESSAGE.THREAD_ID.eq(threadId))
            .and(THREAD_MESSAGE.ROLE.eq(MessageRole.assistant))
            .and(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID.isNotNull)
            .orderBy(THREAD_MESSAGE.CREATED.desc())
            .limit(1)
            .fetchOne()
            ?.get(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID)
    }

    context(tx: TransactionContext)
    suspend fun getMessagesUpTo(threadId: Long, telegramMessageId: Int): List<ThreadMessage> = useTx {
        val cutoffTime = dslContext.select(THREAD_MESSAGE.CREATED)
            .from(THREAD_MESSAGE)
            .where(THREAD_MESSAGE.THREAD_ID.eq(threadId))
            .and(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID.eq(telegramMessageId))
            .fetchOne()
            ?.get(THREAD_MESSAGE.CREATED)
            ?: return@useTx emptyList()

        dslContext.selectFrom(THREAD_MESSAGE)
            .where(THREAD_MESSAGE.THREAD_ID.eq(threadId))
            .and(THREAD_MESSAGE.CREATED.le(cutoffTime))
            .orderBy(THREAD_MESSAGE.CREATED.asc())
            .fetchInto(ThreadMessage::class.java)
    }

    context(tx: TransactionContext)
    suspend fun copyMessagesToThread(messages: List<ThreadMessage>, newThreadId: Long) = useTx {
        for (msg in messages) {
            dslContext.insertInto(THREAD_MESSAGE)
                .set(THREAD_MESSAGE.THREAD_ID, newThreadId)
                .set(THREAD_MESSAGE.ROLE, msg.role)
                .set(THREAD_MESSAGE.CONTENT_TYPE, msg.contentType)
                .set(THREAD_MESSAGE.CONTENT, msg.content)
                .set(THREAD_MESSAGE.TELEGRAM_MESSAGE_ID, msg.telegramMessageId)
                .set(THREAD_MESSAGE.TELEGRAM_USER_ID, msg.telegramUserId)
                .execute()
        }
    }

    context(tx: TransactionContext)
    suspend fun countMessages(): Int = useTx {
        dslContext.fetchCount(THREAD_MESSAGE)
    }
}
