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

fun rule(rule: (Update, BotQueries) -> List<Action>): Rule =
    object : Rule {
        override fun validate(update: Update, queries: BotQueries): List<Action> =
            rule(update, queries)
    }

fun compositeRule(vararg rules: Rule): Rule = rule { update, queries ->
    rules.map { it.validate(update, queries) }.flatten()
}

fun callbackQueryRule(rule: (CallbackQuery, BotQueries) -> List<Action>): Rule = rule { update, queries ->
    if(!update.hasCallbackQuery())
        emptyList()
    else
        rule(update.callbackQuery, queries)
}

fun commandRule(rule: (Message, BotQueries) -> List<Action>): Rule = rule { update, queries ->
    if(!update.hasMessage() ||
        !update.message.hasText() ||
        !update.message.text.startsWith("/"))
        emptyList()
    else rule(update.message, queries)
}

fun commandRule(commandText: String, state: State, rule: (String, Message, BotQueries) -> List<Action>): Rule =
    commandRule rule@ { message, queries ->
        val text = message.text
        if(!text.startsWith(commandText)) return@rule emptyList()
        val botSuffix = "@${state.botUserName}"
        val args = text.substring(commandText.length)
        val filteredArgs = if(args.startsWith(botSuffix)) {
            args.substring(botSuffix.length + 1)
        } else {
            args
        }
        rule(filteredArgs, message, queries)
    }

fun adminCommandRule(commandText: String, state: State, rule: (String, Message, BotQueries) -> List<Action>) : Rule =
    commandRule(commandText, state) { args, message, queries ->
        if(queries.isAdminUser(message.chatId, message.from.id)) {
            rule(args, message, queries)
        } else {
            listOf(DeleteMessageAction(message.chatId, message.messageId))
        }
    }

private val LOGGER = logger<Rule>()

val deleteJoinRule = rule { update, _ ->
    if (!update.message?.newChatMembers.isNullOrEmpty()) {
        LOGGER.info("Delete joined users message ${update.message.newChatMembers}")
        listOf(DeleteMessageAction(update.message.chatId, update.message.messageId))
    } else {
        listOf()
    }
}

fun deleteHelloRule(strings: List<String>) = rule { update, _ ->
    update.anyText { text, message ->
        if (strings.contains(text.toLowerCase())) {
            LOGGER.info("Delete hello message ${message.text}")
            return@rule listOf(DeleteMessageAction(message.chatId, message.messageId))
        }
    }

    listOf()
}

val defaultDeleteHelloRule = deleteHelloRule(listOf(
    "hi",
    "hello",
    "привет"
))

fun deleteSwearingRule(regexs: List<Regex>) = rule { update, _ ->
    update.anyText { text, message ->
        val normalizedText = text.toLowerCase()
        val isSwearing = regexs.any { normalizedText.contains(it) }
        if (isSwearing) {
            LOGGER.info("Delete message with swearing ${message.text}")
            return@rule listOf(DeleteMessageAction(message.chatId, message.messageId))
        }
    }

    listOf()
}

internal fun wordRegex(word: String) = Regex("(?i)\\b$word\\b")

fun resourceDeleteSwearingRule(url: URL) = deleteSwearingRule(
    url.readText()
        .split("\n")
        .filter { it.isNotEmpty() }
        .map(::wordRegex))

val deleteSpamRule = rule { update, _ ->
    update.anyText { text, message ->
        val kick = when {
            text.contains("t.me/joinchat/") -> {
                LOGGER.info("Delete message with join link ${message.text}")
                true
            }
            text.contains("t.cn/") -> {
                LOGGER.info("Delete message with t.cn link ${message.text}")
                true
            }
            else -> false
        }

        if (kick) {
            return@rule listOf(
                DeleteMessageAction(message.chatId, message.messageId),
                KickUserAction(message.chatId, message.from.id)
            )
        }
    }

    listOf()
}
