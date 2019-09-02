package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.utils.fullRef
import org.telegram.telegrambots.meta.api.objects.User
import java.net.URL

/**
 * A rule which deletes all *username joined the group* messages.
 */
suspend fun deleteJoinRule() = rule { update, _ ->
    if (!update.message?.newChatMembers.isNullOrEmpty()) {
        LOGGER.info("Delete joined users message ${update.message.newChatMembers}")
        listOf(DeleteMessageAction(update.message.chatId, update.message.messageId))
    } else {
        listOf()
    }
}

/**
 * A rule which deletes messages containing only specified phrases.
 */
suspend fun deleteSpecificWordRule(tag: String, strings: List<String>) = rule { update, _ ->
    update.anyText { text, message ->
        if (strings.contains(text.toLowerCase())) {
            LOGGER.info("Delete $tag message ${message.text}")
            return@rule listOf(DeleteMessageAction(message.chatId, message.messageId))
        }
    }

    listOf()
}

/**
 * A rule which deletes messages containing only welcome phrases.
 */
suspend fun defaultDeleteHelloRule() = deleteSpecificWordRule("hello", listOf(
    "hi",
    "hello",
    "привет"
))

/**
 * A rule which deletes all messages matching any of regular expressions passed.
 */
suspend fun deleteRegexMatchingRule(regexs: List<Regex>) = rule { update, _ ->
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

/**
 * A rule which deletes messages with swearing. List of swear words should be available as a resource located at [url].
 */
suspend fun deleteSwearingRule(url: URL) = deleteRegexMatchingRule(
    url.readText()
        .split("\n")
        .filter { it.isNotEmpty() }
        .map(::wordRegex))

/**
 * A rule which deletes spam messages.
 */
suspend fun deleteSpamRule() = rule { update, _ ->
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

/**
 * A rule which kicks CAS-banned users which:
 * - Join group
 * - Post new messages
 * - Edit existing messages
 *
 * CAS stands for Combot Anti-spam System and provides an API to check their global ban status.
 */
suspend fun deleteCasBannedRule() = rule { update, queries ->
    val message = update.anyMessage ?: return@rule emptyList()
    val newUsers = message.newChatMembers
    val hasNewUsers = newUsers.isNotEmpty()
    val users: List<User> = if(hasNewUsers) newUsers else listOf(message.from)
    users
        .filter { queries.isCasBanned(it.id) }
        .fold(mutableListOf()) { list, user ->
            LOGGER.info("Delete CAS-banned user ${user.fullRef}. Reason: ${queries.getCasStatusUrl(user.id)}")
            if (!hasNewUsers) {
                list.add(DeleteMessageAction(message.chatId, message.messageId))
            }
            list.add(KickUserAction(message.chatId, user.id))
            list
        }
}

/**
 * A group of commands and rules related to policy implementation.
 */
suspend fun policyRules(swearingResource: URL?) = compositeRule(
    deleteJoinRule(),
    deleteSpamRule(),
    defaultDeleteHelloRule(),
    swearingResource?.let { deleteSwearingRule(it) },
    deleteCasBannedRule()
)
