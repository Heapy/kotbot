package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.rule.Action
import io.heapy.kotbot.bot.rule.DeleteMessageAction
import io.heapy.kotbot.bot.rule.KickUserAction
import io.heapy.kotbot.bot.rule.Rule
import io.heapy.logging.debug
import io.heapy.logging.logger
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
class KotBot(
    private val configuration: BotConfiguration,
    private val rules: List<Rule>,
    private val meterRegistry: MeterRegistry
) : TelegramLongPollingBot() {
    override fun getBotToken() = configuration.token
    override fun getBotUsername() = configuration.name

    private val supervisor = SupervisorJob()

    override fun onUpdateReceived(update: Update) {
        CoroutineScope(supervisor).launch {
            LOGGER.debug { update.toString() }

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
                        LOGGER.error("Exception in rule", e)
                        recordRuleFailure(rule)
                        listOf<Action>()
                    }
                }
                .distinct()
                .forEach(::executeAction)
        }
    }

    internal fun recordRuleTrigger(rule: Rule) {
        meterRegistry.counter(
            "rule.trigger",
            Tags.of("rule", ruleToMetricName(rule))
        ).increment()
    }

    internal fun recordRuleFailure(rule: Rule) {
        val name = ruleToMetricName(rule)
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
            execute(DeleteMessage(action.chatId, action.messageId))
            Unit
        }
        is KickUserAction -> {
            execute(KickChatMember(action.chatId, action.userId))
            Unit
        }
    }

    companion object {
        private val LOGGER = logger<KotBot>()
    }
}
