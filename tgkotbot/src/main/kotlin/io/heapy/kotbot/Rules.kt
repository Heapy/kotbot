package io.heapy.kotbot

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Update
import io.heapy.kotbot.configuration.CasConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.Serializable

interface Rule {
    fun validate(update: Update): Flow<Method<*>>
}

private val LOGGER = logger<Rule>()

class DeleteJoinRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.message?.let { message ->
            if (!message.new_chat_members.isNullOrEmpty()) {
                LOGGER.info("Delete joined users message ${message.new_chat_members}")
                return flowOf(
                    message.delete,
                )
            }
        }

        return emptyFlow()
    }
}

class DeleteHelloRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyText { text, message ->
            if (strings.contains(text.lowercase())) {
                LOGGER.info("Delete hello message ${message.text} from ${message.from?.info}")
                return flowOf(
                    message.delete,
                )
            }
        }

        return emptyFlow()
    }

    companion object {
        private val strings = listOf(
            "hi",
            "hello",
            "привет",
        )
    }
}

class LongTimeNoSeeRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyText { text, message ->
            if (strings.contains(text.lowercase())) {
                LOGGER.info("Delete spam ${message.text} from ${message.from?.info}")
                return flowOf(
                    message.delete,
                    message.banFrom,
                )
            }
        }

        return emptyFlow()
    }

    companion object {
        private val strings = listOf(
            "Long time no see.",
            "What's going on?",
            "How is everything?",
        )
    }
}

class KasperskyCareersRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyText { text, message ->
            if (strings.contains(text.lowercase())) {
                LOGGER.info("Delete spam ${message.text} from ${message.from?.info}")
                return flowOf(
                    message.delete,
                    message.banFrom,
                )
            }
        }

        return emptyFlow()
    }

    companion object {
        private val strings = listOf(
            "careers.kaspersky.ru",
        )
    }
}

class DeleteSwearingRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyText { text, message ->
            val normalizedText = text.lowercase()
            val isSwearing = strings.any { normalizedText.contains(it) }
            if (isSwearing) {
                LOGGER.info("Delete message with swearing ${message.text} from ${message.from?.info}")
                return flowOf(
                    message.delete,
                )
            }
        }

        return emptyFlow()
    }

    companion object {
        internal fun wordRegex(word: String) = Regex("(?i)\\b$word\\b")

        private val strings = readResource("contains.txt")
            ?.split("\n")
            ?.filter { it.isNotEmpty() }
            ?.map(::wordRegex)
            ?: listOf()
    }
}

class DeleteSpamRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyText { text, message ->
            val kick = shorteners.any { shorter ->
                text.contains(shorter)
            }

            if (kick) {
                LOGGER.info("Delete message with shortened link $text from ${message.from?.info}")

                return flowOf(
                    message.delete,
                    message.banFrom,
                )
            }
        }

        return emptyFlow()
    }

    companion object {
        private val shorteners = listOf(
            "tinyurl.com",
            "t.me/joinchat",
            "t.me/+",
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
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyMessage?.let { message ->
            if (message.voice != null) {
                LOGGER.info("Delete voice-message from ${message.from?.info}.")

                return flowOf(
                    message.delete,
                )
            }
        }

        return emptyFlow()
    }
}

/**
 * Rule to delete messages with attached sticker.
 * It's not covered by chat settings, since they don't apply on admins
 */
class DeleteStickersRule : Rule {
    override fun validate(update: Update): Flow<Method<*>> {
        update.anyMessage?.let { message ->
            if (message.sticker != null) {
                LOGGER.info("Delete sticker-message from ${message.from?.info}.")

                return flowOf(
                    message.delete,
                )
            }
        }

        return emptyFlow()
    }
}

class CombotCasRule(
    private val client: HttpClient,
    private val casConfiguration: CasConfiguration,
) : Rule {
    override fun validate(update: Update): Flow<Method<*>> = flow {
        update.anyMessage?.let { message ->
            val userId = message.from!!.id
            if (!casConfiguration.allowlist.contains(userId)) {
                val response = client.get("https://api.cas.chat/check?user_id=$userId").body<CasResponse>()
                if (response.ok) {
                    LOGGER.info("User ${message.from?.info} is CAS banned")
                    emit(message.delete)
                    emit(message.banFrom)
                } else {
                    LOGGER.info("User ${message.from?.info} is NOT CAS banned")
                }
            } else {
                LOGGER.info("User ${message.from?.info} is in CAS allowlist")
            }
        }
    }
}

@Serializable
data class CasResponse(
    val ok: Boolean,
)
