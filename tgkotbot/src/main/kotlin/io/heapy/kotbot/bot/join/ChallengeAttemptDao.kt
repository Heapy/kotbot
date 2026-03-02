package io.heapy.kotbot.bot.join

import io.heapy.kotbot.database.tables.ChallengeAttempt.Companion.CHALLENGE_ATTEMPT
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import java.time.LocalDateTime
import java.util.UUID

class ChallengeAttemptDao {
    context(_: TransactionContext)
    suspend fun insertAttempt(
        sessionId: Long,
        telegramId: Long,
        challengeId: UUID,
        templateKey: String,
        selectedAnswer: String,
        correctAnswer: String,
        isCorrect: Boolean,
        challengeSentAt: LocalDateTime,
        answeredAt: LocalDateTime,
        latencyMs: Long,
    ) = useTx {
        dslContext
            .insertInto(
                CHALLENGE_ATTEMPT,
                CHALLENGE_ATTEMPT.SESSION_ID,
                CHALLENGE_ATTEMPT.TELEGRAM_ID,
                CHALLENGE_ATTEMPT.CHALLENGE_ID,
                CHALLENGE_ATTEMPT.TEMPLATE_KEY,
                CHALLENGE_ATTEMPT.SELECTED_ANSWER,
                CHALLENGE_ATTEMPT.CORRECT_ANSWER,
                CHALLENGE_ATTEMPT.IS_CORRECT,
                CHALLENGE_ATTEMPT.CHALLENGE_SENT_AT,
                CHALLENGE_ATTEMPT.ANSWERED_AT,
                CHALLENGE_ATTEMPT.LATENCY_MS,
            )
            .values(
                sessionId,
                telegramId,
                challengeId,
                templateKey,
                selectedAnswer,
                correctAnswer,
                isCorrect,
                challengeSentAt,
                answeredAt,
                latencyMs,
            )
            .execute()
    }
}
