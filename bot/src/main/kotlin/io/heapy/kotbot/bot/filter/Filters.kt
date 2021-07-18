package io.heapy.kotbot.bot.filter

import io.heapy.komodo.logging.logger
import io.heapy.kotbot.bot.FamilyConfiguration
import io.heapy.kotbot.bot.filter.Filter.Result
import io.heapy.kotbot.bot.filter.Filter.Result.DROP
import io.heapy.kotbot.bot.filter.Filter.Result.TAKE
import org.telegram.telegrambots.meta.api.objects.Update

public interface Filter {
    /**
     * Returns true if this [Update] can be processed by bot, false otherwise.
     */
    public suspend fun predicate(update: Update): Result

    public enum class Result {
        DROP,
        TAKE
    }
}

public class GroupInFamilyFilter(
    private val familyConfiguration: FamilyConfiguration
) : Filter {
    override suspend fun predicate(update: Update): Result {
        return if (inFamily(update)) {
            TAKE
        } else {
            LOGGER.error("Don't process update $update since it's not part of chat family.")
            DROP
        }
    }

    private fun inFamily(update: Update): Boolean {
        val chat = update.message?.chat
            ?: update.editedMessage?.chat
            ?: return true

        return when {
            chat.isSuperGroupChat -> familyConfiguration.ids.contains(chat.id)
            chat.isChannelChat -> familyConfiguration.ids.contains(chat.id)
            chat.isGroupChat -> familyConfiguration.ids.contains(chat.id)
            chat.isUserChat -> true
            else -> true
        }
    }

    public companion object {
        private val LOGGER = logger<GroupInFamilyFilter>()
    }
}
