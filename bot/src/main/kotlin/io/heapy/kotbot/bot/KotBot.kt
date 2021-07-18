package io.heapy.kotbot.bot

import io.heapy.komodo.logging.debug
import io.heapy.komodo.logging.logger
import io.heapy.kotbot.bot.action.Action
import io.heapy.kotbot.bot.action.DeleteMessageAction
import io.heapy.kotbot.bot.action.BanMemberAction
import io.heapy.kotbot.bot.action.ReplyAction
import io.heapy.kotbot.bot.command.Command
import io.heapy.kotbot.bot.command.Command.Access
import io.heapy.kotbot.bot.command.Command.Access.USER
import io.heapy.kotbot.bot.command.Command.Context.GROUP_CHAT
import io.heapy.kotbot.bot.command.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.command.NoopCommand
import io.heapy.kotbot.bot.filter.Filter
import io.heapy.kotbot.bot.rule.Rule
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
public class KotBot(
    private val configuration: BotConfiguration,
    private val rules: List<Rule>,
    private val commands: List<Command>,
    private val filters: List<Filter>,
    private val meterRegistry: MeterRegistry
) : TelegramLongPollingBot() {
    public override fun getBotToken(): String = configuration.token
    public override fun getBotUsername(): String = configuration.name

    private val supervisor = SupervisorJob()

    override fun onUpdateReceived(update: Update) {
        CoroutineScope(supervisor).launch processing@{
            LOGGER.debug { update.toString() }

            if (filters.any { it.predicate(update) == Filter.Result.DROP }) {
                return@processing
            }

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
                        it + DeleteMessageAction(update.message)
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
            update.message.chat.isUserChat -> USER_CHAT
            else -> GROUP_CHAT
        }

        // TODO: Access Control
        val access = USER

        // TODO: can support escaped string with double-quote
        val tokens = update.message.text.split(' ')

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

    public data class UpdateCommand(
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

    public companion object {
        private val LOGGER = logger<KotBot>()
    }
}
