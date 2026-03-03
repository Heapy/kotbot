package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.dao.JobExecutionDao
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.hours

class UserAutoTagJob(
    private val userContextDao: UserContextDao,
    private val jobExecutionDao: JobExecutionDao,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
) {
    fun start() {
        applicationScope.launch {
            while (true) {
                try {
                    val lastExecution = transactionProvider.transaction {
                        jobExecutionDao.getLastExecution(JOB_NAME)
                    }

                    if (lastExecution != null && lastExecution.isAfter(LocalDateTime.now().minusHours(24))) {
                        log.info("User auto-tag job already ran at {}, skipping", lastExecution)
                    } else {
                        val executionId = transactionProvider.transaction {
                            jobExecutionDao.startExecution(JOB_NAME)
                        }

                        try {
                            log.info("Starting user auto-tag job")
                            autoTag()
                            transactionProvider.transaction {
                                jobExecutionDao.completeExecution(executionId)
                            }
                            log.info("User auto-tag job completed")
                        } catch (e: Exception) {
                            transactionProvider.transaction {
                                jobExecutionDao.failExecution(executionId)
                            }
                            throw e
                        }
                    }
                } catch (e: Exception) {
                    log.error("User auto-tag job failed", e)
                }
                delay(1.hours)
            }
        }
    }

    private suspend fun autoTag() {
        val users = transactionProvider.transaction {
            userContextDao.listEligibleForAutoTag(
                minMessageCount = THRESHOLDS.last().first,
                milestoneTags = THRESHOLDS.map { it.second },
            )
        }
        log.info("Processing {} users for auto-tagging", users.size)

        for (user in users) {
            try {
                val targetTag = THRESHOLDS.firstOrNull { (threshold, _) ->
                    user.messageCount >= threshold
                }?.second ?: continue

                if (user.badge == targetTag) {
                    continue
                }

                transactionProvider.transaction {
                    userContextDao.updateBadge(user, targetTag)
                }
                log.info(
                    "Auto-tagged user {} (telegramId={}): {} -> {} (messageCount={})",
                    user.internalId, user.telegramId, user.badge, targetTag, user.messageCount,
                )
            } catch (e: Exception) {
                log.error("Failed to auto-tag user {} (telegramId={})", user.internalId, user.telegramId, e)
            }
        }
    }

    private companion object : Logger() {
        private const val JOB_NAME = "user_auto_tag"

        // Ordered highest-first so firstOrNull picks the best match
        private val THRESHOLDS = listOf(
            100_000 to "object God",
            10_000 to "Null? Never!",
            1_000 to "Sealed Legend",
            100 to "val Chatter",
            10 to "Hello, Kotlin!",
        )
    }
}
