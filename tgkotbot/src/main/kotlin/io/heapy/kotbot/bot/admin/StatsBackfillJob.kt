package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours

class StatsBackfillJob(
    private val userContextDao: UserContextDao,
    private val updateDao: UpdateDao,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
) {
    fun start() {
        applicationScope.launch {
            while (true) {
                try {
                    log.info("Starting stats backfill job")
                    backfill()
                    log.info("Stats backfill job completed")
                } catch (e: Exception) {
                    log.error("Stats backfill job failed", e)
                }
                delay(24.hours)
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

    private companion object : Logger()
}
