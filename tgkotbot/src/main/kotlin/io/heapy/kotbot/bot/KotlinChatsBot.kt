package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.commands.Command.Access.ADMIN
import io.heapy.kotbot.bot.commands.Command.Access.USER
import io.heapy.kotbot.bot.commands.Command.Context.GROUP_CHAT
import io.heapy.kotbot.bot.commands.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.bot.filters.Filter
import io.heapy.kotbot.bot.rules.Actions
import io.heapy.kotbot.bot.rules.Rule
import io.heapy.kotbot.infra.logger
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jooq.DSLContext

class KotlinChatsBot(
    private val kotbot: Kotbot,
    private val rules: List<Rule>,
    private val commands: List<Command>,
    private val filter: Filter,
    private val meterRegistry: MeterRegistry,
    private val admins: List<Long>,
    private val applicationScope: CoroutineScope,
    private val updateDao: UpdateDao,
    private val dslContext: DSLContext,
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
                val result = findAndExecuteCommand(update)
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

    internal suspend fun findAndExecuteCommand(update: Update): Boolean {
        // Command accepts text only in a message, no update message supported
        update.message?.text?.run {
            try {
                val command = commands.find { command ->
                    command.name == update.name
                            && command.context == update.context
                            && command.access.isAllowed(update.access)
                } ?: return false

                command.execute(kotbot, update)

                return true
            } catch (e: Exception) {
                log.error("Exception in command. Update: {}", update, e)
            }
        }

        return false
    }

    private val Update.context: Command.Context
        get() = when (message?.chat?.type) {
            "private" -> USER_CHAT
            else -> GROUP_CHAT
        }

    private val Update.access: Command.Access
        get() = message?.from?.id?.let { id ->
            if (admins.contains(id)) ADMIN else USER
        } ?: USER

    private val Update.name: String?
        get() = message?.text?.split(' ')?.getOrNull(0)

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
