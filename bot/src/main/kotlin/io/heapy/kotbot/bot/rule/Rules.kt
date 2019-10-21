package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.anyText
import io.heapy.logging.logger
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
interface Rule {
    fun validate(update: Update): List<Action>
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
            val kick = shorteners.any { shorter ->
                text.contains(shorter)
            }

            if (kick) {
                LOGGER.info("Delete message with shortened link $text")

                return listOf(
                    DeleteMessageAction(message.chatId, message.messageId),
                    KickUserAction(message.chatId, message.from.id)
                )
            }
        }

        return listOf()
    }

    companion object {
        private val shorteners = listOf(
            "tinyurl.com",
            "t.me/joinchat",
            "t.cn",
            "bit.ly",
            "tgraph.io"
        )
    }
}

/**
 * Rule to remove messages with attached audio.
 */
class DeleteVoiceMessageRule : Rule {

    override fun validate(update: Update): List<Action> {
        val message = update.message

        if (message.hasVoice()) {
            LOGGER.info("Delete voice-message from @${message.from.userName}.")

            return DeleteMessageAction(message).only()
        }

        return noActions()
    }
}
