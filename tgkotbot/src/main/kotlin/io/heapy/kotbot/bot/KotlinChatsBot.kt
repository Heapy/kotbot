package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.komok.tech.logging.logger
import io.heapy.kotbot.bot.commands.CommandResolver
import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.bot.filters.Filter
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.rules.RuleExecutor
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class KotlinChatsBot(
    private val kotbot: Kotbot,
    private val filter: Filter,
    private val meterRegistry: MeterRegistry,
    private val applicationScope: CoroutineScope,
    private val updateDao: UpdateDao,
    private val commandResolver: CommandResolver,
    private val ruleExecutor: RuleExecutor,
    private val transactionProvider: TransactionProvider,
) {
    suspend fun start() {
        val updateReceived = meterRegistry.counter("update.received")
        val updateFiltered = meterRegistry.counter("update.filtered")
        val updateSaved = meterRegistry.counter("update.saved")

        kotbot.receiveUpdates()
            .onEach {
                updateReceived.increment()
            }
            .filter {
                filter.predicate(it).also { passed ->
                    if (!passed) {
                        updateFiltered.increment()
                    }
                }
            }
            .onEach { update ->
                saveUpdateAsync(updateSaved, update)
            }
            .onEach { update ->
                val result = commandResolver.findAndExecuteCommand(update)
                if (!result) {
                    ruleExecutor.executeRules(update)
                }
            }
            .launchIn(applicationScope)
    }

    private val job = SupervisorJob()

    private fun saveUpdateAsync(updateSaved: Counter, update: Update) = CoroutineScope(job).launch {
        try {
            saveUpdate(update)
            updateSaved.increment()
        } catch (e: Exception) {
            log.error("Failed to save update: {}", update, e)
            throw e
        }
    }

    private suspend fun saveUpdate(update: Update) = transactionProvider.transaction {
        updateDao.saveRawUpdate(
            kotbot.json.encodeToString(Update.serializer(), update),
        )
    }

    private companion object : Logger()
}

internal suspend fun <Request : Method<Request, Result>, Result> Kotbot.executeSafely(
    method: Request,
): Result? {
    val log = logger<Kotbot>()

    return try {
        execute(method)
    } catch (e: Exception) {
        log.error("Method {} failed: {}", method, e.message, e)
        null
    }
}
