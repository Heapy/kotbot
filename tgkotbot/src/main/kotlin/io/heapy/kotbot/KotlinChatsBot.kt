package io.heapy.kotbot

import io.heapy.kotbot.Command.Access.ADMIN
import io.heapy.kotbot.Command.Access.USER
import io.heapy.kotbot.Command.Context.GROUP_CHAT
import io.heapy.kotbot.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.receiveUpdates
import io.heapy.kotbot.dao.UpdateDao
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
    private val main: CompletableDeferred<String>,
) {
    suspend fun start() {
        kotbot.receiveUpdates()
            .filter(filter::predicate)
            .onEach { update ->
                saveUpdateAsync(update)
                val result = findAndExecuteCommand(update)
                if (!result) {
                    executeRules(update)
                }
            }
            .launchIn(applicationScope)
            .invokeOnCompletion { cause ->
                when (cause) {
                    null -> main.complete("Updates completed")
                    is CancellationException -> main.complete("Updates cancelled")
                    else -> main.cancel("Updates failed", cause)
                }
            }
    }

    private val job = SupervisorJob()

    private fun saveUpdateAsync(update: Update) = CoroutineScope(job).launch {
        saveUpdate(update)
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
        val actions = Actions()
        return rules.forEach { rule ->
            rule.validate(kotbot, update, actions)
        }
    }

    internal fun recordRuleTrigger(rule: Rule) {
        meterRegistry.counter(
            "rule.trigger",
            Tags.of("rule", ruleToMetricName(rule))
        ).increment()
    }

    internal fun recordRuleFailure(rule: Rule) {
        meterRegistry.counter(
            "rule.error",
            Tags.of("rule", ruleToMetricName(rule))
        ).increment()
    }

    internal fun ruleToMetricName(rule: Rule): String {
        return rule::class.simpleName ?: "UnknownRule"
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
