package io.heapy.kotbot.bot.rule

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.heapy.kotbot.bot.CasConfiguration
import io.heapy.kotbot.bot.action.Action
import io.heapy.kotbot.bot.action.DeleteMessageAction
import io.heapy.kotbot.bot.action.KickUserAction
import io.heapy.kotbot.bot.anyMessage
import io.heapy.kotbot.bot.anyText
import io.heapy.logging.logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
interface Rule {
    fun validate(update: Update): Flow<Action>
}

private val LOGGER = logger<Rule>()

class DeleteJoinRule : Rule {
    override fun validate(update: Update): Flow<Action> {
        if (!update.message?.newChatMembers.isNullOrEmpty()) {
            LOGGER.info("Delete joined users message ${update.message.newChatMembers}")
            return flowOf(DeleteMessageAction(update.message.chatId, update.message.messageId))
        }

        return emptyFlow()
    }
}

class DeleteHelloRule : Rule {
    override fun validate(update: Update): Flow<Action> {
        update.anyText { text, message ->
            if (strings.contains(text.toLowerCase())) {
                LOGGER.info("Delete hello message ${message.text}")
                return flowOf(DeleteMessageAction(message.chatId, message.messageId))
            }
        }

        return emptyFlow()
    }

    companion object {
        private val strings = listOf(
            "hi",
            "hello",
            "привет"
        )
    }
}

class LongTimeNoSeeRule : Rule {
    override fun validate(update: Update): Flow<Action> {
        update.anyText { text, message ->
            if (strings.contains(text.toLowerCase())) {
                LOGGER.info("Delete spam ${message.text}")
                return flowOf(
                    DeleteMessageAction(message.chatId, message.messageId),
                    KickUserAction(message.chatId, message.from.id)
                )
            }
        }

        return emptyFlow()
    }

    companion object {
        private val strings = listOf(
            "Long time no see.",
            "What's going on?",
            "How is everything?"
        )
    }
}

class DeleteSwearingRule : Rule {
    override fun validate(update: Update): Flow<Action> {
        update.anyText { text, message ->
            val normalizedText = text.toLowerCase()
            val isSwearing = strings.any { normalizedText.contains(it) }
            if (isSwearing) {
                LOGGER.info("Delete message with swearing ${message.text}")
                return flowOf(DeleteMessageAction(message.chatId, message.messageId))
            }
        }

        return emptyFlow()
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
    override fun validate(update: Update): Flow<Action> {
        update.anyText { text, message ->
            val kick = shorteners.any { shorter ->
                text.contains(shorter)
            }

            if (kick) {
                LOGGER.info("Delete message with shortened link $text")

                return flowOf(
                    DeleteMessageAction(message.chatId, message.messageId),
                    KickUserAction(message.chatId, message.from.id)
                )
            }
        }

        return emptyFlow()
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
    override fun validate(update: Update): Flow<Action> {
        update.anyMessage?.let { message ->
            if (message.hasVoice()) {
                LOGGER.info("Delete voice-message from @${message.from.userName}.")

                return flowOf(DeleteMessageAction(message))
            }
        }

        return emptyFlow()
    }
}

class CombotCasRule(
    private val client: HttpClient,
    private val casConfiguration: CasConfiguration
) : Rule {
    override fun validate(update: Update): Flow<Action> = flow {
        update.anyMessage?.let { message ->
            val userId = message.from.id
            if (!casConfiguration.allowlist.contains(userId.toLong())) {
                val response = client.get<CasResponse>("https://api.cas.chat/check?user_id=$userId")
                if (response.ok) {
                    LOGGER.info("User $userId is CAS banned")
                    emit(DeleteMessageAction(message))
                    emit(KickUserAction(message.chatId, userId))
                } else {
                    LOGGER.info("User $userId is NOT CAS banned")
                }
            } else {
                LOGGER.info("User $userId is in CAS allowlist")
            }
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class CasResponse(
    val ok: Boolean
)
