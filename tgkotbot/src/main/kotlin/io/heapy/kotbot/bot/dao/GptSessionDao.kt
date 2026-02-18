package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.GptSession.Companion.GPT_SESSION
import io.heapy.kotbot.database.tables.GptSessionMessage.Companion.GPT_SESSION_MESSAGE
import io.heapy.kotbot.database.tables.pojos.GptSessionMessage
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import java.time.LocalDateTime

class GptSessionDao {
    context(_: TransactionContext)
    suspend fun createSession(
        userId: Long,
        groupChatId: Long,
        waitMessageId: Int,
    ): Long? = useTx {
        dslContext
            .insertInto(
                GPT_SESSION,
                GPT_SESSION.USER_ID,
                GPT_SESSION.GROUP_CHAT_ID,
                GPT_SESSION.WAIT_MESSAGE_ID,
                GPT_SESSION.CREATED,
            )
            .values(
                userId,
                groupChatId,
                waitMessageId,
                LocalDateTime.now(),
            )
            .returning(GPT_SESSION.ID)
            .fetchOne()
            ?.getValue(GPT_SESSION.ID)
    }

    context(_: TransactionContext)
    suspend fun setPreviewMessage(
        sessionId: Long,
        previewChatId: Long,
        previewMessageId: Int,
    ) = useTx {
        dslContext
            .update(GPT_SESSION)
            .set(GPT_SESSION.PREVIEW_CHAT_ID, previewChatId)
            .set(GPT_SESSION.PREVIEW_MESSAGE_ID, previewMessageId)
            .where(GPT_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun findByPreviewMessage(
        previewChatId: Long,
        previewMessageId: Int,
    ): GptSessionData? = useTx {
        dslContext
            .select(GPT_SESSION.ID, GPT_SESSION.GROUP_CHAT_ID, GPT_SESSION.WAIT_MESSAGE_ID)
            .from(GPT_SESSION)
            .where(GPT_SESSION.PREVIEW_CHAT_ID.eq(previewChatId))
            .and(GPT_SESSION.PREVIEW_MESSAGE_ID.eq(previewMessageId))
            .and(GPT_SESSION.STATUS.eq("ACTIVE"))
            .fetchOne()
            ?.let {
                GptSessionData(
                    sessionId = it.get(GPT_SESSION.ID)!!,
                    groupChatId = it.get(GPT_SESSION.GROUP_CHAT_ID)!!,
                    waitMessageId = it.get(GPT_SESSION.WAIT_MESSAGE_ID)!!,
                )
            }
    }

    context(_: TransactionContext)
    suspend fun updateStatus(
        sessionId: Long,
        status: String,
    ) = useTx {
        dslContext
            .update(GPT_SESSION)
            .set(GPT_SESSION.STATUS, status)
            .where(GPT_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun addMessage(
        sessionId: Long,
        role: String,
        content: String,
    ) = useTx {
        dslContext
            .insertInto(
                GPT_SESSION_MESSAGE,
                GPT_SESSION_MESSAGE.SESSION_ID,
                GPT_SESSION_MESSAGE.ROLE,
                GPT_SESSION_MESSAGE.CONTENT,
                GPT_SESSION_MESSAGE.CREATED,
            )
            .values(
                sessionId,
                role,
                content,
                LocalDateTime.now(),
            )
            .execute()
    }

    context(_: TransactionContext)
    suspend fun getMessages(
        sessionId: Long,
    ): List<GptSessionMessage> = useTx {
        dslContext
            .selectFrom(GPT_SESSION_MESSAGE)
            .where(GPT_SESSION_MESSAGE.SESSION_ID.eq(sessionId))
            .orderBy(GPT_SESSION_MESSAGE.CREATED.asc())
            .fetchInto(GptSessionMessage::class.java)
    }
}

data class GptSessionData(
    val sessionId: Long,
    val groupChatId: Long,
    val waitMessageId: Int,
)