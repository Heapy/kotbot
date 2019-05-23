package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.rule.Action
import io.heapy.kotbot.bot.rule.DeleteMessageAction
import io.heapy.kotbot.bot.rule.KickUserAction
import io.heapy.kotbot.bot.rule.Rule
import io.heapy.logging.debug
import io.heapy.logging.logger
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
class KotBot(
    private val configuration: BotConfiguration,
    private val rules: List<Rule>
) : TelegramLongPollingBot() {
    override fun getBotToken() = configuration.token
    override fun getBotUsername() = configuration.name

    override fun onUpdateReceived(update: Update) {
        LOGGER.debug { update.toString() }

        rules
            .flatMap { rule -> rule.validate(update) }
            .distinct()
            .forEach(::executeAction)
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
