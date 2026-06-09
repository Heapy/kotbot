package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.challenge.ChallengeGenerator
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.ApproveChatJoinRequest
import io.heapy.kotbot.bot.method.DeclineChatJoinRequest
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.ChatJoinRequest
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.infra.jdbc.TransactionContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

/**
 * First stage of the join flow: handles an incoming [ChatJoinRequest] — rejects banned users,
 * auto-approves already-verified ones, enforces the per-group cooldown, then creates a session
 * and sends the first challenge.
 */
class JoinRequestHandler(
    private val kotbot: Kotbot,
    private val challengeGenerator: ChallengeGenerator,
    private val joinSessionDao: JoinSessionDao,
    private val verifiedUserDao: VerifiedUserDao,
    private val resolvedConfig: ResolvedJoinChallengeConfig,
    private val userContextDao: UserContextDao,
    private val messages: JoinMessages,
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
            val _ = kotbot.executeSafely(
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
            val _ = kotbot.executeSafely(
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

        // Skip if the user is already in the appeal flow in any chat of the group (idempotent)
        val appeal = joinSessionDao.findAppealSession(telegramId, groupChatIds)
        if (appeal != null) {
            log.info("User {} already has an appeal session in chat group (status={}), skipping", telegramId, appeal.status)
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
                val _ = kotbot.executeSafely(
                    DeclineChatJoinRequest(
                        chat_id = LongChatId(chatId),
                        user_id = telegramId,
                    )
                )
                val _ = kotbot.executeSafely(
                    SendMessage(
                        chat_id = LongChatId(userChatId),
                        text = messages.formatCooldown(remaining),
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
        val _ = joinSessionDao.updateChallenge(
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
                text = messages.formatChallengeMessage(challenge.snippet, maxAttempts, 0),
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = messages.buildAnswerKeyboard(challengeId, challenge.options),
            )
        )

        if (message != null) {
            val _ = joinSessionDao.updateMessageId(sessionId, message.message_id)
        }

        log.info("Sent join challenge to user {} for chat {} (session={})", telegramId, chatId, sessionId)
    }

    private companion object : Logger()
}
