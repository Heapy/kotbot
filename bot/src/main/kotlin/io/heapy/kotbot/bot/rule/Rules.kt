package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.BotQueries
import io.heapy.kotbot.bot.anyText
import io.heapy.logging.logger
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
interface Rule {
    fun validate(update: Update): List<Action> = emptyList()
    fun validate(update: Update, queries: BotQueries): List<Action> = validate(update)
}

private val LOGGER = logger<Rule>()

class DeleteJoinRule : Rule {
    override fun validate(update: Update): List<Action> {
        if (!update.message?.newChatMembers.isNullOrEmpty()) {
            LOGGER.info("Delete joined users message ${update.message.newChatMembers}")
            return listOf(DeleteMessageAction(update.message.chatId, update.message.messageId))
        }

        return listOf()
    }
}

class DeleteHelloRule : Rule {
    override fun validate(update: Update): List<Action> {
        update.anyText { text, message ->
            if (strings.contains(text.toLowerCase())) {
                LOGGER.info("Delete hello message ${message.text}")
                return listOf(DeleteMessageAction(message.chatId, message.messageId))
            }
        }

        return listOf()
    }

    companion object {
        private val strings = listOf(
            "hi",
            "hello",
            "привет"
        )
    }
}

class DeleteSwearingRule : Rule {
    override fun validate(update: Update): List<Action> {
        update.anyText { text, message ->
            val normalizedText = text.toLowerCase()
            val isSwearing = strings.any { normalizedText.contains(it) }
            if (isSwearing) {
                LOGGER.info("Delete message with swearing ${message.text}")
                return listOf(DeleteMessageAction(message.chatId, message.messageId))
            }
        }

        return listOf()
    }

    companion object {
        internal fun wordRegex(word: String) = Regex("(?i)\\b$word\\b")

        private val strings = DeleteSwearingRule::class.java.classLoader
            .getResource("contains.txt")
            ?.readText()
            ?.split("\n")
            ?.filter { it.isNotEmpty() }
            ?.map(::wordRegex)
            ?: listOf()
    }
}

class DeleteSpamRule : Rule {
    override fun validate(update: Update): List<Action> {
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
                return listOf(
                    DeleteMessageAction(message.chatId, message.messageId),
                    KickUserAction(message.chatId, message.from.id)
                )
            }
        }

        return listOf()
    }
}
