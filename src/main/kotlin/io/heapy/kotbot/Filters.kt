package io.heapy.kotbot

import io.heapy.kotbot.bot.ApiUpdate
import io.heapy.kotbot.configuration.KniwnChatsConfiguration

fun interface Filter {
    /**
     * Returns true if this [ApiUpdate] can be processed by bot, false otherwise.
     */
    suspend fun predicate(update: ApiUpdate): Boolean

    companion object
}

/**
 * Create single filter from multiple filters.
 * Returns false if any of the filters returns false.
 */
fun Filter.Companion.combine(filters: List<Filter>): Filter {
    return Filter { update ->
        filters.all { filter -> filter.predicate(update) }
    }
}

class KnownChatsFilter(
    private val knownChatsConfiguration: KniwnChatsConfiguration
) : Filter {
    override suspend fun predicate(update: ApiUpdate): Boolean {
        return isWellKnown(update).also { decision ->
            if (!decision) {
                LOGGER.error("Don't process update $update since it's not part of chat family.")
            }
        }
    }

    private fun isWellKnown(update: ApiUpdate): Boolean {
        val chat = update.message?.chat
            ?: update.edited_message?.chat
            ?: return true

        return when(chat.type) {
            "supergroup" -> knownChatsConfiguration.ids.contains(chat.id)
            "channel" -> knownChatsConfiguration.ids.contains(chat.id)
            "group" -> knownChatsConfiguration.ids.contains(chat.id)
            "private" -> true
            else -> false
        }
    }

    companion object {
        private val LOGGER = logger<KnownChatsFilter>()
    }
}
