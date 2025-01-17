package io.heapy.kotbot.bot.use_case.history

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LogUpdatesService(
    meterRegistry: MeterRegistry,
    private val updateDao: UpdateDao,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
) {
    private val updatesSaved = meterRegistry.counter("update.saved")

    suspend fun save(
        response: HttpResponse,
    ) {
        val update = response.bodyAsText()

        if (update != EMPTY_UPDATE) {
            saveAsync(update)
        }
    }

    private fun saveAsync(
        update: String,
    ) {
        applicationScope.launch {
            log.info("Saving raw response to database")
            try {
                transactionProvider.transaction {
                    updateDao.saveRawUpdate(update)
                }
                updatesSaved.increment()
            } catch (e: Exception) {
                log.error("Failed to save update: {}", update, e)
                throw e
            }
        }
    }

    private companion object : Logger() {
        //language=JSON
        private const val EMPTY_UPDATE = """{"ok":true,"result":[]}"""
    }
}

