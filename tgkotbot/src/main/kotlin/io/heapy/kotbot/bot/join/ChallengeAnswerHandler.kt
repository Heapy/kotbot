package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.challenge.ChallengeGenerator
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.ApproveChatJoinRequest
import io.heapy.kotbot.bot.method.DeclineChatJoinRequest
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.RestrictChatMember
import io.heapy.kotbot.bot.model.CallbackQuery
import io.heapy.kotbot.bot.model.ChatPermissions
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.database.enums.JoinSessionStatus
import io.heapy.kotbot.database.enums.VerificationSource
import io.heapy.kotbot.infra.jdbc.TransactionContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

/**
 * Second stage of the join flow: scores a tapped challenge answer. On success it gates the user
 * through CAS before approving; wrong-but-retryable answers get a fresh challenge; exhausted
 * attempts decline the join.
 */
class ChallengeAnswerHandler(
    private val kotbot: Kotbot,
    private val challengeGenerator: ChallengeGenerator,
    private val joinSessionDao: JoinSessionDao,
    private val verifiedUserDao: VerifiedUserDao,
    private val challengeAttemptDao: ChallengeAttemptDao,
    private val casClient: CasClient,
    private val notificationService: NotificationService,
    private val messages: JoinMessages,
) {
    context(_: TransactionContext)
    suspend fun handleChallengeAnswer(
        callbackQuery: CallbackQuery,
        challengeId: UUID,
        selectedIndex: Int,
    ) {
        val telegramId = callbackQuery.from.id

        // Lookup session by challenge_id
        val session = joinSessionDao.findByChallengeId(challengeId)
        if (session == null) {
            log.warn("No active session found for challenge_id={}", challengeId)
            return
        }

        // Verify user identity
        if (session.telegramId != telegramId) {
            log.warn("User {} tried to answer challenge for user {}", telegramId, session.telegramId)
            return
        }

        val options = session.options ?: return
        if (selectedIndex !in options.indices) {
            log.warn("Invalid option index {} for session {}", selectedIndex, session.id)
            return
        }

        val selectedAnswer = options[selectedIndex]
        val isCorrect = selectedAnswer == session.correctAnswer
        val answeredAt = LocalDateTime.now()
        val challengeSentAt = session.challengeSentAt ?: answeredAt
        val latencyMs = Duration.between(challengeSentAt, answeredAt).toMillis()

        // Log attempt
        val _ = challengeAttemptDao.insertAttempt(
            sessionId = session.id,
            telegramId = telegramId,
            challengeId = challengeId,
            templateKey = session.templateKey ?: "unknown",
            selectedAnswer = selectedAnswer,
            correctAnswer = session.correctAnswer ?: "",
            isCorrect = isCorrect,
            challengeSentAt = challengeSentAt,
            answeredAt = answeredAt,
            latencyMs = latencyMs,
        )

        // Increment attempts
        val _ = joinSessionDao.incrementAttempts(session.id)
        val attemptsUsed = session.attemptsUsed + 1

        if (isCorrect) {
            // Challenge passed: gate on CAS before approving (only reached for non-verified joiners).
            when (val casResult = casClient.check(telegramId)) {
                is CasResult.Flagged -> handleCasFlagged(session, telegramId, casResult)
                CasResult.Clean -> approvePassed(session, telegramId)
            }
            log.info("User {} passed challenge for chat {} (attempt {})", telegramId, session.chatId, attemptsUsed)
        } else if (attemptsUsed < session.maxAttempts) {
            // Wrong but attempts left: generate new challenge
            val newChallenge = challengeGenerator.generate()
            val newChallengeId = UUID.randomUUID()
            val now = LocalDateTime.now()
            val _ = joinSessionDao.updateChallenge(
                sessionId = session.id,
                challengeId = newChallengeId,
                templateKey = newChallenge.templateKey,
                seed = newChallenge.seed,
                snippet = newChallenge.snippet,
                correctAnswer = newChallenge.correctAnswer,
                options = newChallenge.options,
                challengeSentAt = now,
            )
            editMessage(
                session = session,
                text = messages.formatChallengeMessage(
                    newChallenge.snippet,
                    session.maxAttempts,
                    attemptsUsed,
                    prefix = "❌ Wrong answer. Try again!\n\n",
                ),
                keyboard = messages.buildAnswerKeyboard(newChallengeId, newChallenge.options),
            )
            log.info("User {} wrong answer for chat {} (attempt {}/{})", telegramId, session.chatId, attemptsUsed, session.maxAttempts)
        } else {
            // Exhausted attempts: mark FAILED, decline join
            val _ = joinSessionDao.finishSession(session.id, JoinSessionStatus.FAILED)
            val _ = kotbot.executeSafely(
                DeclineChatJoinRequest(
                    chat_id = LongChatId(session.chatId),
                    user_id = telegramId,
                )
            )
            editMessage(
                session = session,
                text = messages.formatExhausted(attemptsUsed),
                keyboard = null,
            )
            log.info("User {} exhausted attempts for chat {} ({}/{})", telegramId, session.chatId, attemptsUsed, session.maxAttempts)
        }
    }

    context(_: TransactionContext)
    private suspend fun approvePassed(
        session: JoinSessionData,
        telegramId: Long,
    ) {
        val _ = joinSessionDao.finishSession(session.id, JoinSessionStatus.PASSED)
        val _ = verifiedUserDao.insertVerified(
            telegramId = telegramId,
            source = VerificationSource.CHALLENGE,
            sessionId = session.id,
        )
        val _ = kotbot.executeSafely(
            ApproveChatJoinRequest(
                chat_id = LongChatId(session.chatId),
                user_id = telegramId,
            )
        )
        editMessage(
            session = session,
            text = messages.formatCorrect(),
            keyboard = null,
        )
    }

    context(_: TransactionContext)
    private suspend fun handleCasFlagged(
        session: JoinSessionData,
        telegramId: Long,
        casResult: CasResult.Flagged,
    ) {
        val _ = joinSessionDao.setAwaitingAppeal(session.id, casResult.offenses, casResult.timeAdded, casResult.messages)

        // Show the CAS reason + appeal instructions BEFORE approving: Telegram only guarantees the
        // user_chat_id DM window while the join request is still pending.
        editMessage(
            session = session,
            text = messages.formatCasFlaggedMessage(casResult),
            keyboard = null,
        )

        // RestrictChatMember only works once the user is a member, so approve first, then restrict.
        val _ = kotbot.executeSafely(
            ApproveChatJoinRequest(
                chat_id = LongChatId(session.chatId),
                user_id = telegramId,
            )
        )
        val restricted = kotbot.executeSafely(
            RestrictChatMember(
                chat_id = LongChatId(session.chatId),
                user_id = telegramId,
                permissions = readOnlyPermissions,
                use_independent_chat_permissions = true,
            )
        )
        if (restricted == null) {
            // Optimistic flow: if we cannot restrict (e.g. missing can_restrict_members), the user is
            // admitted unrestricted -- alert admins to review manually rather than fail closed.
            val _ = notificationService.notifyAdmins(
                "⚠️ Could not restrict CAS-flagged user $telegramId in chat ${session.chatId} after a passed challenge. Please review manually.",
                null,
            )
        }
        log.info("User {} flagged by CAS (reasons={}) for chat {}, admitted read-only pending appeal (session={})", telegramId, casResult.reasons, session.chatId, session.id)
    }

    private suspend fun editMessage(
        session: JoinSessionData,
        text: String,
        keyboard: InlineKeyboardMarkup?,
    ) {
        val messageId = session.messageId ?: return
        val _ = kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(session.userChatId),
                message_id = messageId,
                text = text,
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = keyboard,
            )
        )
    }

    private val readOnlyPermissions = ChatPermissions(
        can_send_messages = false,
        can_send_audios = false,
        can_send_documents = false,
        can_send_photos = false,
        can_send_videos = false,
        can_send_video_notes = false,
        can_send_voice_notes = false,
        can_send_polls = false,
        can_send_other_messages = false,
        can_add_web_page_previews = false,
        can_react_to_messages = false,
    )

    private companion object : Logger()
}
