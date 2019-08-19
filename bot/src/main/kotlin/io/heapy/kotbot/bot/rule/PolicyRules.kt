package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.*
import java.net.URL

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

fun policyRules(swearingResource: URL?) = compositeRule(
    deleteJoinRule,
    deleteSpamRule,
    defaultDeleteHelloRule,
    swearingResource?.let(::resourceDeleteSwearingRule)
)
