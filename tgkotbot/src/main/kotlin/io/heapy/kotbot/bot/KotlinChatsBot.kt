package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.commands.CommandResolver
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.bot.filters.Filter
import io.heapy.kotbot.bot.rules.Actions
import io.heapy.kotbot.bot.rules.Rule
import io.heapy.kotbot.infra.logger
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jooq.DSLContext

class KotlinChatsBot(
    private val kotbot: Kotbot,
    private val rules: List<Rule>,
    private val filter: Filter,
    private val meterRegistry: MeterRegistry,
    private val applicationScope: CoroutineScope,
    private val updateDao: UpdateDao,
    private val dslContext: DSLContext,
    private val commandResolver: CommandResolver,
) {
    private val updateReceived = meterRegistry.counter("update.received")
    private val updateFiltered = meterRegistry.counter("update.filtered")
    private val updateSaved = meterRegistry.counter("update.saved")

    suspend fun start() {
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
                saveUpdateAsync(update)
            }
            .onEach { update ->
                val result = commandResolver.findAndExecuteCommand(update)
                if (!result) {
                    executeRules(update)
                }
            }
            .launchIn(applicationScope)
    }

    private val job = SupervisorJob()

    private fun saveUpdateAsync(update: Update) = CoroutineScope(job).launch {
        try {
            saveUpdate(update)
            updateSaved.increment()
        } catch (e: Exception) {
            log.error("Failed to save update: {}", update, e)
            throw e
        }
    }

    private suspend fun saveUpdate(update: Update) = with(dslContext) {
        updateDao.saveRawUpdate(
            kotbot.json.encodeToString(Update.serializer(), update),
        )
    }

    internal suspend fun executeRules(update: Update) {
        val actions = Actions(meterRegistry)
        return rules.forEach { rule ->
            rule.validate(kotbot, update, actions)
        }
    }
}

private val log = logger<KotlinChatsBot>()

internal suspend fun <Request : Method<Request, Result>, Result> Kotbot.executeSafely(
    method: Request,
): Result? {
    return try {
        execute(method)
    } catch (e: Exception) {
        log.error("Method {} failed: {}", method, e.message, e)
        null
    }
}
