package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.JoinChallengeAnswerCallbackData
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.challenge.ChallengeGenerator
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.ApproveChatJoinRequest
import io.heapy.kotbot.bot.method.DeclineChatJoinRequest
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.CallbackQuery
import io.heapy.kotbot.bot.model.ChatJoinRequest
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.database.enums.JoinSessionStatus
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.database.enums.VerificationSource
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

class JoinChallengeProcessor(
    private val kotbot: Kotbot,
    private val challengeGenerator: ChallengeGenerator,
    private val joinSessionDao: JoinSessionDao,
    private val verifiedUserDao: VerifiedUserDao,
    private val challengeAttemptDao: ChallengeAttemptDao,
    private val resolvedConfig: ResolvedJoinChallengeConfig,
    private val callbackDataService: CallbackDataService,
    private val markdown: Markdown,
    private val userContextDao: UserContextDao,
) {
    context(_: TransactionContext)
    suspend fun handleJoinRequest(joinRequest: ChatJoinRequest) {
        val telegramId = joinRequest.from.id
        val chatId = joinRequest.chat.id
        val userChatId = joinRequest.user_chat_id

        // Reject banned users immediately
        val userContext = userContextDao.get(telegramId)
        if (userContext?.status == TelegramUserStatus.BANNED) {
            log.info("Banned user {} attempted to join chat {}, declining", telegramId, chatId)
            kotbot.executeSafely(
                DeclineChatJoinRequest(
                    chat_id = LongChatId(chatId),
                    user_id = telegramId,
                )
            )
            return
        }

        // Auto-approve if already verified
        val verified = verifiedUserDao.findByTelegramId(telegramId)
        if (verified != null) {
            log.info("User {} already verified, auto-approving join to chat {}", telegramId, chatId)
            kotbot.executeSafely(
                ApproveChatJoinRequest(
                    chat_id = LongChatId(chatId),
                    user_id = telegramId,
                )
            )
            return
        }

        // Resolve chat group: all chats in the same group share sessions and cooldowns
        val groupChatIds = resolvedConfig.chatIdsInGroup(chatId)

        // Skip if active session already exists in any chat of the group (idempotent)
        val existing = joinSessionDao.findActiveSession(telegramId, groupChatIds)
        if (existing != null) {
            log.info("Active session already exists for user {} in chat group (session chat={})", telegramId, existing.chatId)
            return
        }

        // Enforce retry cooldown after failed/expired session in any chat of the group
        val lastFailed = joinSessionDao.findLastFinishedSession(telegramId, groupChatIds)
        if (lastFailed?.finishedAt != null) {
            val cooldownEnd = lastFailed.finishedAt.plus(resolvedConfig.retryCooldown.toJavaDuration())
            val now = LocalDateTime.now()
            if (now.isBefore(cooldownEnd)) {
                val remaining = Duration.between(now, cooldownEnd).toKotlinDuration().inWholeMinutes.minutes
                log.info("User {} in cooldown until {} for chat {}, declining", telegramId, cooldownEnd, chatId)
                kotbot.executeSafely(
                    DeclineChatJoinRequest(
                        chat_id = LongChatId(chatId),
                        user_id = telegramId,
                    )
                )
                kotbot.executeSafely(
                    SendMessage(
                        chat_id = LongChatId(userChatId),
                        text = markdown.escape("❌ You can retry the challenge in $remaining. Please try again later."),
                        parse_mode = ParseMode.MarkdownV2.name,
                    )
                )
                return
            }
        }

        // Create session
        val maxAttempts = resolvedConfig.maxAttemptsForChat(chatId)
        val expiresAt = LocalDateTime.now().plus(resolvedConfig.sessionTimeout.toJavaDuration())
        val sessionId = joinSessionDao.createSession(
            telegramId = telegramId,
            chatId = chatId,
            userChatId = userChatId,
            maxAttempts = maxAttempts,
            expiresAt = expiresAt,
        )

        // Generate and store challenge
        val challenge = challengeGenerator.generate()
        val challengeId = UUID.randomUUID()
        val now = LocalDateTime.now()
        joinSessionDao.updateChallenge(
            sessionId = sessionId,
            challengeId = challengeId,
            templateKey = challenge.templateKey,
            seed = challenge.seed,
            snippet = challenge.snippet,
            correctAnswer = challenge.correctAnswer,
            options = challenge.options,
            challengeSentAt = now,
        )

        // Send challenge message
        val message = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(userChatId),
                text = formatChallengeMessage(challenge.snippet, maxAttempts, 0),
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = buildKeyboard(challengeId, challenge.options),
            )
        )

        if (message != null) {
            joinSessionDao.updateMessageId(sessionId, message.message_id)
        }

        log.info("Sent join challenge to user {} for chat {} (session={})", telegramId, chatId, sessionId)
    }

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
        challengeAttemptDao.insertAttempt(
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
        joinSessionDao.incrementAttempts(session.id)
        val attemptsUsed = session.attemptsUsed + 1

        if (isCorrect) {
            // Mark PASSED, insert VERIFIED_USER, approve join
            joinSessionDao.finishSession(session.id, JoinSessionStatus.PASSED)
            verifiedUserDao.insertVerified(
                telegramId = telegramId,
                source = VerificationSource.CHALLENGE,
                sessionId = session.id,
            )
            kotbot.executeSafely(
                ApproveChatJoinRequest(
                    chat_id = LongChatId(session.chatId),
                    user_id = telegramId,
                )
            )
            editMessage(
                session = session,
                text = markdown.escape("✅ Correct! Your join request has been approved. Welcome!"),
                keyboard = null,
            )
            log.info("User {} passed challenge for chat {} (attempt {})", telegramId, session.chatId, attemptsUsed)
        } else if (attemptsUsed < session.maxAttempts) {
            // Wrong but attempts left: generate new challenge
            val newChallenge = challengeGenerator.generate()
            val newChallengeId = UUID.randomUUID()
            val now = LocalDateTime.now()
            joinSessionDao.updateChallenge(
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
                text = formatChallengeMessage(
                    newChallenge.snippet,
                    session.maxAttempts,
                    attemptsUsed,
                    prefix = "❌ Wrong answer. Try again!\n\n",
                ),
                keyboard = buildKeyboard(newChallengeId, newChallenge.options),
            )
            log.info("User {} wrong answer for chat {} (attempt {}/{})", telegramId, session.chatId, attemptsUsed, session.maxAttempts)
        } else {
            // Exhausted attempts: mark FAILED, decline join
            joinSessionDao.finishSession(session.id, JoinSessionStatus.FAILED)
            kotbot.executeSafely(
                DeclineChatJoinRequest(
                    chat_id = LongChatId(session.chatId),
                    user_id = telegramId,
                )
            )
            editMessage(
                session = session,
                text = markdown.escape("❌ Wrong answer. You've used all $attemptsUsed attempts. Your join request has been declined."),
                keyboard = null,
            )
            log.info("User {} exhausted attempts for chat {} ({}/{})", telegramId, session.chatId, attemptsUsed, session.maxAttempts)
        }
    }

    private suspend fun editMessage(
        session: JoinSessionData,
        text: String,
        keyboard: InlineKeyboardMarkup?,
    ) {
        val messageId = session.messageId ?: return
        kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(session.userChatId),
                message_id = messageId,
                text = text,
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = keyboard,
            )
        )
    }

    private fun formatChallengeMessage(
        snippet: String,
        maxAttempts: Int,
        attemptsUsed: Int,
        prefix: String = "",
    ): String = markdown.escape(buildString {
        append(prefix)
        appendLine("To join the chat, please solve this Kotlin challenge:")
        appendLine()
        appendLine("What is the output of the following code?")
        appendLine()
        appendLine("```kotlin")
        appendLine(snippet)
        appendLine("```")
        appendLine()
        append("Attempts: $attemptsUsed/$maxAttempts")
    })

    context(_: TransactionContext)
    private suspend fun buildKeyboard(
        challengeId: UUID,
        options: List<String>,
    ): InlineKeyboardMarkup = InlineKeyboardMarkup(
        inline_keyboard = options.mapIndexed { index, option ->
            listOf(
                InlineKeyboardButton(
                    text = option,
                    callback_data = callbackDataService.insert(
                        JoinChallengeAnswerCallbackData(
                            challengeId = challengeId.toString(),
                            selectedIndex = index,
                        )
                    ),
                )
            )
        },
    )

    private companion object : Logger()
}
