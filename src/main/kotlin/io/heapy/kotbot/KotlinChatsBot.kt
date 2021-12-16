package io.heapy.kotbot

import io.heapy.kotbot.Command.Access
import io.heapy.kotbot.Command.Access.USER
import io.heapy.kotbot.Command.Context.GROUP_CHAT
import io.heapy.kotbot.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.ApiMethod
import io.heapy.kotbot.bot.Update
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.receiveUpdates
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
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
    }

    internal suspend fun findAndExecuteCommand(update: Update): Boolean {
        // Command accepts text only in message, no update message supported
        val text = update.message?.text ?: return false

        // find command-like message or return
        commands.find { command ->
            text.startsWith(command.info.name)
        } ?: return false

        try {
            // parse command-like message
            val info = updateToCommandInfo(update)

            // find actual command
            val command = commands.find { command ->
                command.info.name == info.name
                        && command.info.context == info.context
                        && command.info.arity == info.arity
                        && command.info.access >= info.access
            } ?: NoopCommand

            command.execute(update, listOf())
                .toList()
                .let {
                    if (info.context == GROUP_CHAT) {
                        it + DeleteMessageAction(
                            chatId = update.message?.chat?.id!!,
                            messageId = update.message?.message_id!!
                        )
                    } else {
                        it
                    }
                }
                .distinct()
                .forEach(::executeAction)
        } catch (e: Exception) {
            LOGGER.error("Exception in command. Update: {}", update, e)
            listOf<Action>()
        }

        return true
    }

    internal fun updateToCommandInfo(update: Update): UpdateCommand {
        val context = when {
            update.message?.chat.isUserChat -> USER_CHAT
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
            .forEach(::executeAction)
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

    internal fun executeAction(action: Action): Unit = when (action) {
        is DeleteMessageAction -> {
            execute(DeleteMessage(action.chatId.toString(), action.messageId))
            Unit
        }
        is BanMemberAction -> {
            execute(BanChatMember(action.chatId.toString(), action.userId))
            Unit
        }
        is ReplyAction -> {
            execute(SendMessage(action.chatId.toString(), action.message))
            Unit
        }
    }

    private fun execute(method: ApiMethod<*>) {
        println("method called: $method")
    }

    companion object {
        private val LOGGER = logger<KotlinChatsBot>()
    }
}
