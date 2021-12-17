package io.heapy.kotbot

import io.heapy.kotbot.Command.Access
import io.heapy.kotbot.Command.Access.USER
import io.heapy.kotbot.Command.Context.GROUP_CHAT
import io.heapy.kotbot.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.ApiMethod
import io.heapy.kotbot.bot.DeleteMessage
import io.heapy.kotbot.bot.Update
import io.heapy.kotbot.bot.Kotbot
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
    private val meterRegistry: MeterRegistry
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
        update.message?.let { message ->
            // Command accepts text only in message, no update message supported
            message.text?.let { text ->
                try {
                    commands.find { command ->
                        text.startsWith(command.info.name)
                    } ?: return false

                    // parse command-like message
                    val info = updateToCommandInfo(update)

                    // find actual command
                    val command = commands.find { command ->
                        command.info.name == info.name
                            && command.info.context == info.context
                            && command.info.arity == info.arity
                            && command.info.access >= info.access
                    } ?: NoopCommand

                    command.execute(message, update, listOf())
                        .toList()
                        .let {
                            if (info.context == GROUP_CHAT) {
                                it + DeleteMessage(
                                    chat_id = message.chat.id.toString(),
                                    message_id = message.message_id
                                )
                            } else {
                                it
                            }
                        }
                        .distinct()
                        .forEach {
                            execute(it)
                        }

                    return true
                } catch (e: Exception) {
                    LOGGER.error("Exception in command. Update: {}", update, e)
                }
            }
        }
        return false
    }

    internal fun updateToCommandInfo(update: Update): UpdateCommand {
        val context = when (update.message?.chat?.type) {
            "private" -> USER_CHAT
            else -> GROUP_CHAT
        }

        // TODO: Access Control
        val access = USER

        // TODO: can support escaped string with double-quote
        val tokens = update.message?.text?.split(' ')!!

        val name = tokens[0]
        val arity = tokens.size - 1
        val args = tokens.drop(1)

        return UpdateCommand(
            name = name,
            arity = arity,
            access = access,
            context = context,
            args = args
        )
    }

    data class UpdateCommand(
        override val name: String,
        override val arity: Int,
        override val context: Command.Context,
        override val access: Access,
        val args: List<String>
    ) : Command.Info

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
                    LOGGER.error("Exception in rule {}", rule, e)
                    recordRuleFailure(rule)
                    listOf()
                }
            }
            .distinct()
            .forEach {
                execute(it)
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

    internal suspend fun execute(method: ApiMethod<*>) {
        try {
            kotbot.execute(method)
        } catch (e: Exception) {
            LOGGER.error("Method {} failed: {}", method, e.message, e)
        }
    }

    companion object {
        private val LOGGER = logger<KotlinChatsBot>()
    }
}
