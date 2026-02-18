package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.dao.JobExecutionDao
import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.hours

class StatsBackfillJob(
    private val userContextDao: UserContextDao,
    private val updateDao: UpdateDao,
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
                        log.info("Stats backfill job already ran at {}, skipping", lastExecution)
                    } else {
                        val executionId = transactionProvider.transaction {
                            jobExecutionDao.startExecution(JOB_NAME)
                        }

                        try {
                            log.info("Starting stats backfill job")
                            backfill()
                            transactionProvider.transaction {
                                jobExecutionDao.completeExecution(executionId)
                            }
                            log.info("Stats backfill job completed")
                        } catch (e: Exception) {
                            transactionProvider.transaction {
                                jobExecutionDao.failExecution(executionId)
                            }
                            throw e
                        }
                    }
                } catch (e: Exception) {
                    log.error("Stats backfill job failed", e)
                }
                delay(1.hours)
            }
        }
    }

    private suspend fun backfill() {
        val users = transactionProvider.transaction {
            userContextDao.listAll(limit = Int.MAX_VALUE, offset = 0)
        }
        log.info("Backfilling stats for {} users", users.size)

        for (user in users) {
            try {
                transactionProvider.transaction {
                    val stats = updateDao.getUserStatsFromRaw(user.telegramId)
                    val firstSeen = stats.firstSeen
                    if (firstSeen != null) {
                        val created = if (firstSeen < user.created) firstSeen else user.created
                        userContextDao.backfillStats(
                            internalId = user.internalId,
                            created = created,
                            messageCount = stats.messageCount,
                        )
                        log.info(
                            "Backfilled user {} (telegramId={}): created={}, messageCount={}",
                            user.internalId, user.telegramId, created, stats.messageCount,
                        )
                    } else {
                        log.info("User {} (telegramId={}) has no raw updates, skipping", user.internalId, user.telegramId)
                    }
                }
            } catch (e: Exception) {
                log.error("Failed to backfill stats for user {} (telegramId={})", user.internalId, user.telegramId, e)
            }
        }
    }

    private companion object : Logger() {
        private const val JOB_NAME = "stats_backfill"
    }
}
