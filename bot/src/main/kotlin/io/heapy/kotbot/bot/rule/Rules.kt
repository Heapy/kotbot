package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.*
import io.heapy.logging.logger
import org.telegram.telegrambots.meta.api.objects.*
import java.net.URL

/**
 * @author Ruslan Ibragimov
 */
interface Rule {
    fun validate(update: Update, queries: BotQueries): List<Action>
}

/**
 * Creates generic rule for processing incoming messages and events.
 */
fun rule(rule: (Update, BotQueries) -> List<Action>): Rule =
    object : Rule {
        override fun validate(update: Update, queries: BotQueries): List<Action> =
            rule(update, queries)
    }

/**
 * Creates rule which applies all passed [rules] sequentially.
 */
fun compositeRule(vararg rules: Rule?): Rule = rule { update, queries ->
    rules.mapNotNull { it?.validate(update, queries) }.flatten()
}

/**
 * Creates rule processing incoming [CallbackQuery] (result of pressing callback button, inline keyboard button, etc).
 */
fun callbackQueryRule(rule: (CallbackQuery, BotQueries) -> List<Action>): Rule = rule { update, queries ->
    if(!update.hasCallbackQuery())
        emptyList()
    else
        rule(update.callbackQuery, queries)
}

/**
 * Creates rule processing commands. A command is a message of the following format:
 *
 * `/command_name command_args...`
 */
fun commandRule(rule: (Message, BotQueries) -> List<Action>): Rule = rule { update, queries ->
    if(!update.hasMessage() ||
        !update.message.hasText() ||
        !update.message.text.startsWith("/"))
        emptyList()
    else rule(update.message, queries)
}

/**
 * Creates rule processing command named [commandText]. Handles both commands with and without bot username appended.
 */
fun commandRule(commandText: String, state: State, rule: (args: String, Message, BotQueries) -> List<Action>): Rule =
    if(commandText.length > COMMAND_NAME_MAX_LENGTH)
        error("Command name must not be more than $COMMAND_NAME_MAX_LENGTH characters long.")
    else
        commandRule { message, queries ->
            val text = message.text
            val botCommandText = "$commandText@${state.botUserName}"
            when {
                text == commandText || text == botCommandText ->
                    rule("", message, queries)

                text.startsWith("$commandText ") ->
                    rule(text.substring(commandText.length + 1), message, queries)

                text.startsWith("$botCommandText ") ->
                    rule(text.substring(botCommandText.length + 1), message, queries)

                else -> emptyList()
            }
        }

/**
 * Creates rule processing command named [commandText]. Validates that the user invoking the command is an administrator
 * of the chat where the command is invoked.
 */
fun adminCommandRule(commandText: String, state: State, rule: (String, Message, BotQueries) -> List<Action>) : Rule =
    commandRule(commandText, state) { args, message, queries ->
        if(queries.isAdminUser(message.chatId, message.from.id)) {
            rule(args, message, queries)
        } else {
            listOf(DeleteMessageAction(message.chatId, message.messageId))
        }
    }

private val COMMAND_NAME_MAX_LENGTH = 32

internal val LOGGER = logger<Rule>()
