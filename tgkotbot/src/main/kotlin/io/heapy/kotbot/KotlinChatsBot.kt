package io.heapy.kotbot

import io.heapy.kotbot.Command.Access.ADMIN
import io.heapy.kotbot.Command.Access.USER
import io.heapy.kotbot.Command.Context.GROUP_CHAT
import io.heapy.kotbot.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.receiveUpdates
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList

class KotlinChatsBot(
    private val kotbot: Kotbot,
    private val rules: List<Rule>,
    private val commands: List<Command>,
    private val filter: Filter,
    private val meterRegistry: MeterRegistry,
    private val admins: List<Long>,
) {
    suspend fun start() {
        kotbot.receiveUpdates()
            .filter(filter::predicate)
            .onEach { update ->
                val result = findAndExecuteCommand(update)
                if (!result) {
                    executeRules(update)
                }
            }
            .collect()
    }

    internal suspend fun findAndExecuteCommand(update: Update): Boolean {
        // Command accepts text only in message, no update message supported
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
        rules
            .map { rule -> rule to rule.validate(update) }
            .flatMap { (rule, flow) ->
                try {
                    flow.toList().also { actions ->
                        if (actions.isNotEmpty()) {
                            recordRuleTrigger(rule)
                        }
                    }
                } catch (e: Exception) {
                    log.error("Exception in rule {}", rule, e)
                    recordRuleFailure(rule)
                    listOf()
                }
            }
            .distinct()
            .forEach {
                kotbot.executeSafely(it)
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

internal suspend fun <Response> Kotbot.executeSafely(
    method: Method<Response>
): Response? {
    return try {
        execute(method)
    } catch (e: Exception) {
        log.error("Method {} failed: {}", method, e.message, e)
        null
    }
}
