package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.DeclineChatJoinRequest
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.heapy.kotbot.infra.markdown.Markdown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes

class JoinChallengeExpiryJob(
    private val joinSessionDao: JoinSessionDao,
    private val kotbot: Kotbot,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
    private val markdown: Markdown,
) {
    fun start() {
        applicationScope.launch {
            while (true) {
                try {
                    processExpiredSessions()
                } catch (e: Exception) {
                    log.error("Failed to process expired join sessions", e)
                }
                delay(1.minutes)
            }
        }
    }

    private suspend fun processExpiredSessions() {
        val expired = transactionProvider.transaction {
            joinSessionDao.findAndExpireSessions(LocalDateTime.now())
        }

        if (expired.isEmpty()) return

        log.info("Expiring {} join challenge sessions", expired.size)

        for (session in expired) {
            try {
                kotbot.executeSafely(
                    DeclineChatJoinRequest(
                        chat_id = LongChatId(session.chatId),
                        user_id = session.telegramId,
                    )
                )

                val messageId = session.messageId
                if (messageId != null) {
                    kotbot.executeSafely(
                        EditMessageText(
                            chat_id = LongChatId(session.userChatId),
                            message_id = messageId,
                            text = markdown.escape("⏰ Challenge expired. Your join request has been declined. Please try again."),
                            parse_mode = ParseMode.MarkdownV2.name,
                            reply_markup = null,
                        )
                    )
                }
            } catch (e: Exception) {
                log.error("Failed to process expired session {}", session.id, e)
            }
        }
    }

    private companion object : Logger()
}
