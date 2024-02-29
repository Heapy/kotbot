package io.heapy.kotbot

import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.configuration.KnownChatsConfiguration

fun interface Filter {
    /**
     * Returns true if this [Update] can be processed by bot, false otherwise.
     */
    suspend fun predicate(update: Update): Boolean

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
    conf: KnownChatsConfiguration,
) : Filter {
    private val knownGroups = conf.ids.values + conf.admin
    private val blockedGroups = conf.blocked

    override suspend fun predicate(update: Update): Boolean {
        return isWellKnown(update).also { wellKnown ->
            if (!wellKnown && !isBlocked(update)) {
                log.error("Don't process update $update since it's not part of chat family.")
            }
        }
    }

    private fun isBlocked(update: Update): Boolean {
        val chat = update.message?.chat
            ?: update.edited_message?.chat
            ?: return true

        return when(chat.type) {
            "supergroup" -> blockedGroups.contains(chat.id)
            "channel" -> blockedGroups.contains(chat.id)
            "group" -> blockedGroups.contains(chat.id)
            "private" -> false
            else -> true
        }
    }

    private fun isWellKnown(update: Update): Boolean {
        val chat = update.message?.chat
            ?: update.edited_message?.chat
            ?: return true

        return when(chat.type) {
            "supergroup" -> knownGroups.contains(chat.id)
            "channel" -> knownGroups.contains(chat.id)
            "group" -> knownGroups.contains(chat.id)
            "private" -> true
            else -> false
        }
    }

    companion object {
        private val log = logger<KnownChatsFilter>()
    }
}
