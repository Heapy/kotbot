package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.dao.JobExecutionDao
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ExistingMemberBootstrapJob(
    private val userContextDao: UserContextDao,
    private val verifiedUserDao: VerifiedUserDao,
    private val jobExecutionDao: JobExecutionDao,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
) {
    fun start() {
        applicationScope.launch {
            try {
                val lastExecution = transactionProvider.transaction {
                    jobExecutionDao.getLastExecution(JOB_NAME)
                }

                if (lastExecution == null) {
                    startExecution()
                } else {
                    log.info("Existing member bootstrap job already ran at {}, skipping", lastExecution)
                }
            } catch (e: Exception) {
                log.error("Existing member bootstrap job failed", e)
            }
        }
    }

    private suspend fun startExecution() {
        val executionId = transactionProvider.transaction {
            jobExecutionDao.startExecution(JOB_NAME)
        }

        try {
            log.info("Starting existing member bootstrap job")
            bootstrap()
            transactionProvider.transaction {
                jobExecutionDao.completeExecution(executionId)
            }
            log.info("Existing member bootstrap job completed")
        } catch (e: Exception) {
            transactionProvider.transaction {
                jobExecutionDao.failExecution(executionId)
            }
            throw e
        }
    }

    private suspend fun bootstrap() {
        val users = transactionProvider.transaction {
            userContextDao.listAll(limit = Int.MAX_VALUE, offset = 0)
        }
        log.info("Bootstrapping {} existing users as verified", users.size)

        for (user in users) {
            try {
                transactionProvider.transaction {
                    verifiedUserDao.insertExistingMember(user.telegramId)
                }
            } catch (e: Exception) {
                log.error("Failed to bootstrap user {} (telegramId={})", user.internalId, user.telegramId, e)
            }
        }
    }

    private companion object : Logger() {
        private const val JOB_NAME = "existing_member_bootstrap"
    }
}
