package io.heapy.kotbot

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.configuration.CasConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

interface Rule {
    suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    )
}

class Actions {
    private val actions = mutableSetOf<Method<*, *>>()

    suspend fun <Request : Method<Request, Result>, Result> runIfNew(
        action: Request,
        block: suspend (Request) -> Unit,
    ) {
        if (action !in actions) {
            actions += action
            block(action)
        }
    }
}

private val log = logger<Rule>()

class DeleteJoinRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {

        update.message?.let { message ->
            if (!message.new_chat_members.isNullOrEmpty()) {
                actions.runIfNew(message.delete) {
                    log.info("Delete joined users message ${message.new_chat_members}")
                    kotbot.executeSafely(it)
                }
            }
        }
    }
}

class DeleteHelloRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyText { text, message ->
            if (strings.contains(text.lowercase())) {
                actions.runIfNew(message.delete) {
                    log.info("Delete hello message ${message.text} from ${message.from?.info}")
                    kotbot.executeSafely(it)
                }
            }
        }
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
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyText { text, message ->
            if (strings.contains(text.lowercase())) {
                log.info("Delete spam ${message.text} from ${message.from?.info}")
                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
                actions.runIfNew(message.banFrom) {
                    kotbot.executeSafely(it)
                }
            }
        }
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
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyText { text, message ->
            if (strings.contains(text.lowercase())) {
                log.info("Delete spam ${message.text} from ${message.from?.info}")
                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
                actions.runIfNew(message.banFrom) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }

    companion object {
        private val strings = listOf(
            "careers.kaspersky.ru",
        )
    }
}

class DeleteSwearingRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyText { text, message ->
            val normalizedText = text.lowercase()
            val isSwearing = strings.any { normalizedText.contains(it) }
            if (isSwearing) {
                log.info("Delete message with swearing ${message.text} from ${message.from?.info}")
                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
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
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyText { text, message ->
            val kick = shorteners.any { shorter ->
                text.contains(shorter)
            }

            if (kick) {
                log.info("Delete message with shortened link $text from ${message.from?.info}")

                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
                actions.runIfNew(message.banFrom) {
                    kotbot.executeSafely(it)
                }
            }
        }
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
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            if (message.voice != null) {
                log.info("Delete voice message from ${message.from?.info}.")

                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }
}

/**
 * Rule to remove messages with attached audio.
 */
class DeleteVideoNoteRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            if (message.video_note != null) {
                log.info("Delete video note message from ${message.from?.info}.")

                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }
}

/**
 * Rule to delete messages with attached sticker.
 * It's not covered by chat settings, since they don't apply on admins
 */
class DeleteStickersRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            if (message.sticker != null) {
                log.info("Delete sticker-message from ${message.from?.info}.")

                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }
}

class DeletePropagandaRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            val hasOffensiveText = listOfNotNull(
                message.from?.first_name,
                message.from?.last_name,
                message.text
            ).any {
                it.contains("🇷🇺")
            }

            if (hasOffensiveText) {
                log.info("Delete flag-message from ${message.from?.info}.")

                actions.runIfNew(message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }
}

class CombotCasRule(
    private val client: HttpClient,
    private val casConfiguration: CasConfiguration,
) : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            val userId = message.from!!.id
            if (!casConfiguration.allowlist.contains(userId)) {
                val response = client.get("https://api.cas.chat/check?user_id=$userId").body<CasResponse>()
                if (response.ok) {
                    log.info("User ${message.from?.info} is CAS banned")
                    actions.runIfNew(message.delete) {
                        kotbot.executeSafely(it)
                    }
                    actions.runIfNew(message.banFrom) {
                        kotbot.executeSafely(it)
                    }
                } else {
                    log.info("User ${message.from?.info} is NOT CAS banned")
                }
            } else {
                log.info("User ${message.from?.info} is in CAS allowlist")
            }
        }
    }
}

@Serializable
data class CasResponse(
    val ok: Boolean,
)
