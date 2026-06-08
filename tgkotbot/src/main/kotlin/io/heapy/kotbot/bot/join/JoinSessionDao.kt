package io.heapy.kotbot.bot.join

import io.heapy.kotbot.database.enums.JoinSessionStatus
import io.heapy.kotbot.database.tables.JoinSession.Companion.JOIN_SESSION
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.jooq.JSONB
import java.time.LocalDateTime
import java.util.*

data class JoinSessionData(
    val id: Long,
    val telegramId: Long,
    val chatId: Long,
    val userChatId: Long,
    val status: JoinSessionStatus,
    val maxAttempts: Int,
    val attemptsUsed: Int,
    val expiresAt: LocalDateTime,
    val finishedAt: LocalDateTime?,
    val challengeId: UUID?,
    val templateKey: String?,
    val seed: Long?,
    val snippet: String?,
    val correctAnswer: String?,
    val options: List<String>?,
    val challengeSentAt: LocalDateTime?,
    val messageId: Int?,
    val appealText: String?,
    val casOffenses: Int?,
    val casTimeAdded: String?,
    val casMessages: List<String>?,
)

class JoinSessionDao {
    context(_: TransactionContext)
    suspend fun createSession(
        telegramId: Long,
        chatId: Long,
        userChatId: Long,
        maxAttempts: Int,
        expiresAt: LocalDateTime,
    ): Long = useTx {
        dslContext
            .insertInto(
                JOIN_SESSION,
                JOIN_SESSION.TELEGRAM_ID,
                JOIN_SESSION.CHAT_ID,
                JOIN_SESSION.USER_CHAT_ID,
                JOIN_SESSION.MAX_ATTEMPTS,
                JOIN_SESSION.EXPIRES_AT,
                JOIN_SESSION.CREATED_AT,
            )
            .values(
                telegramId,
                chatId,
                userChatId,
                maxAttempts,
                expiresAt,
                LocalDateTime.now(),
            )
            .returning(JOIN_SESSION.ID)
            .fetchOne()
            ?.getValue(JOIN_SESSION.ID)
            ?: error("Failed to insert join session")
    }

    context(_: TransactionContext)
    suspend fun findActiveSession(
        telegramId: Long,
        chatIds: List<Long>,
    ): JoinSessionData? = useTx {
        dslContext
            .selectFrom(JOIN_SESSION)
            .where(JOIN_SESSION.TELEGRAM_ID.eq(telegramId))
            .and(JOIN_SESSION.CHAT_ID.`in`(chatIds))
            .and(JOIN_SESSION.STATUS.eq(JoinSessionStatus.ACTIVE))
            .fetchOne()
            ?.toData()
    }

    context(_: TransactionContext)
    suspend fun findByChallengeId(
        challengeId: UUID,
    ): JoinSessionData? = useTx {
        dslContext
            .selectFrom(JOIN_SESSION)
            .where(JOIN_SESSION.CHALLENGE_ID.eq(challengeId))
            .and(JOIN_SESSION.STATUS.eq(JoinSessionStatus.ACTIVE))
            .fetchOne()
            ?.toData()
    }

    context(_: TransactionContext)
    suspend fun updateMessageId(
        sessionId: Long,
        messageId: Int,
    ) = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.MESSAGE_ID, messageId)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun incrementAttempts(
        sessionId: Long,
    ) = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.ATTEMPTS_USED, JOIN_SESSION.ATTEMPTS_USED.plus(1))
            .where(JOIN_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun updateChallenge(
        sessionId: Long,
        challengeId: UUID,
        templateKey: String,
        seed: Long,
        snippet: String,
        correctAnswer: String,
        options: List<String>,
        challengeSentAt: LocalDateTime,
    ) = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.CHALLENGE_ID, challengeId)
            .set(JOIN_SESSION.TEMPLATE_KEY, templateKey)
            .set(JOIN_SESSION.SEED, seed)
            .set(JOIN_SESSION.SNIPPET, snippet)
            .set(JOIN_SESSION.CORRECT_ANSWER, correctAnswer)
            .set(JOIN_SESSION.OPTIONS, JSONB.valueOf(
                Json.encodeToString(
                    ListSerializer(String.serializer()),
                    options,
                )
            ))
            .set(JOIN_SESSION.CHALLENGE_SENT_AT, challengeSentAt)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun finishSession(
        sessionId: Long,
        status: JoinSessionStatus,
        finishedAt: LocalDateTime = LocalDateTime.now(),
    ) = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.STATUS, status)
            .set(JOIN_SESSION.FINISHED_AT, finishedAt)
            .setNull(JOIN_SESSION.CHALLENGE_ID)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun findLastFinishedSession(
        telegramId: Long,
        chatIds: List<Long>,
    ): JoinSessionData? = useTx {
        dslContext
            .selectFrom(JOIN_SESSION)
            .where(JOIN_SESSION.TELEGRAM_ID.eq(telegramId))
            .and(JOIN_SESSION.CHAT_ID.`in`(chatIds))
            .and(JOIN_SESSION.STATUS.`in`(JoinSessionStatus.FAILED, JoinSessionStatus.EXPIRED))
            .orderBy(JOIN_SESSION.FINISHED_AT.desc())
            .limit(1)
            .fetchOne()
            ?.toData()
    }

    context(_: TransactionContext)
    suspend fun findById(
        sessionId: Long,
    ): JoinSessionData? = useTx {
        dslContext
            .selectFrom(JOIN_SESSION)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .fetchOne()
            ?.toData()
    }

    /** A non-finished session already in the appeal flow, used as an idempotency guard. */
    context(_: TransactionContext)
    suspend fun findAppealSession(
        telegramId: Long,
        chatIds: List<Long>,
    ): JoinSessionData? = useTx {
        dslContext
            .selectFrom(JOIN_SESSION)
            .where(JOIN_SESSION.TELEGRAM_ID.eq(telegramId))
            .and(JOIN_SESSION.CHAT_ID.`in`(chatIds))
            .and(JOIN_SESSION.STATUS.`in`(JoinSessionStatus.AWAITING_APPEAL, JoinSessionStatus.APPEAL_PENDING))
            .limit(1)
            .fetchOne()
            ?.toData()
    }

    context(_: TransactionContext)
    suspend fun findAwaitingAppealByUser(
        telegramId: Long,
        userChatId: Long,
    ): JoinSessionData? = useTx {
        dslContext
            .selectFrom(JOIN_SESSION)
            .where(JOIN_SESSION.TELEGRAM_ID.eq(telegramId))
            .and(JOIN_SESSION.USER_CHAT_ID.eq(userChatId))
            .and(JOIN_SESSION.STATUS.eq(JoinSessionStatus.AWAITING_APPEAL))
            .limit(1)
            .fetchOne()
            ?.toData()
    }

    /** Marks a passed-but-CAS-flagged session as awaiting the user's written appeal. */
    context(_: TransactionContext)
    suspend fun setAwaitingAppeal(
        sessionId: Long,
        casOffenses: Int?,
        casTimeAdded: String?,
        casMessages: List<String>?,
    ) = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.STATUS, JoinSessionStatus.AWAITING_APPEAL)
            .set(JOIN_SESSION.CAS_OFFENSES, casOffenses)
            .set(JOIN_SESSION.CAS_TIME_ADDED, casTimeAdded)
            .set(JOIN_SESSION.CAS_MESSAGES, casMessages?.let {
                JSONB.valueOf(
                    Json.encodeToString(
                        ListSerializer(String.serializer()),
                        it,
                    )
                )
            })
            .setNull(JOIN_SESSION.CHALLENGE_ID)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun submitAppeal(
        sessionId: Long,
        text: String,
    ) = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.STATUS, JoinSessionStatus.APPEAL_PENDING)
            .set(JOIN_SESSION.APPEAL_TEXT, text)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .execute()
    }

    /**
     * Atomically resolves a pending appeal, but only while it is still [JoinSessionStatus.APPEAL_PENDING].
     * Returns the updated session, or null when no row matched (a stale/double admin click) so the
     * caller can avoid reversing a previous decision.
     */
    context(_: TransactionContext)
    suspend fun decidePendingAppeal(
        sessionId: Long,
        finalStatus: JoinSessionStatus,
        finishedAt: LocalDateTime = LocalDateTime.now(),
    ): JoinSessionData? = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.STATUS, finalStatus)
            .set(JOIN_SESSION.FINISHED_AT, finishedAt)
            .where(JOIN_SESSION.ID.eq(sessionId))
            .and(JOIN_SESSION.STATUS.eq(JoinSessionStatus.APPEAL_PENDING))
            .returning()
            .fetchOne()
            ?.toData()
    }

    context(_: TransactionContext)
    suspend fun findAndExpireSessions(
        now: LocalDateTime,
    ): List<JoinSessionData> = useTx {
        dslContext
            .update(JOIN_SESSION)
            .set(JOIN_SESSION.STATUS, JoinSessionStatus.EXPIRED)
            .set(JOIN_SESSION.FINISHED_AT, now)
            .setNull(JOIN_SESSION.CHALLENGE_ID)
            .where(JOIN_SESSION.STATUS.eq(JoinSessionStatus.ACTIVE))
            .and(JOIN_SESSION.EXPIRES_AT.le(now))
            .returning()
            .fetch()
            .map { it.toData() }
    }

    private fun org.jooq.Record.toData(): JoinSessionData {
        val optionsJson = get(JOIN_SESSION.OPTIONS)
        val optionsList = optionsJson?.let {
            Json.decodeFromString(
                ListSerializer(String.serializer()),
                it.data(),
            )
        }
        val casMessagesJson = get(JOIN_SESSION.CAS_MESSAGES)
        val casMessages = casMessagesJson?.let {
            Json.decodeFromString(
                ListSerializer(String.serializer()),
                it.data(),
            )
        }
        return JoinSessionData(
            id = get(JOIN_SESSION.ID)!!,
            telegramId = get(JOIN_SESSION.TELEGRAM_ID)!!,
            chatId = get(JOIN_SESSION.CHAT_ID)!!,
            userChatId = get(JOIN_SESSION.USER_CHAT_ID)!!,
            status = get(JOIN_SESSION.STATUS)!!,
            maxAttempts = get(JOIN_SESSION.MAX_ATTEMPTS)!!,
            attemptsUsed = get(JOIN_SESSION.ATTEMPTS_USED)!!,
            expiresAt = get(JOIN_SESSION.EXPIRES_AT)!!,
            finishedAt = get(JOIN_SESSION.FINISHED_AT),
            challengeId = get(JOIN_SESSION.CHALLENGE_ID),
            templateKey = get(JOIN_SESSION.TEMPLATE_KEY),
            seed = get(JOIN_SESSION.SEED),
            snippet = get(JOIN_SESSION.SNIPPET),
            correctAnswer = get(JOIN_SESSION.CORRECT_ANSWER),
            options = optionsList,
            challengeSentAt = get(JOIN_SESSION.CHALLENGE_SENT_AT),
            messageId = get(JOIN_SESSION.MESSAGE_ID),
            appealText = get(JOIN_SESSION.APPEAL_TEXT),
            casOffenses = get(JOIN_SESSION.CAS_OFFENSES),
            casTimeAdded = get(JOIN_SESSION.CAS_TIME_ADDED),
            casMessages = casMessages,
        )
    }
}
