package io.heapy.kotbot.bot.filters

import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration
import io.heapy.kotbot.infra.debug.PrettyPrint
import io.heapy.kotbot.infra.logger

class KnownChatsFilter(
    private val config: KnownChatsConfiguration,
    private val notificationService: NotificationService,
    private val prettyPrint: PrettyPrint,
) : Filter {
    private val knownGroups = config.allowedGroups.values + config.adminPrivateGroup

    override suspend fun predicate(update: Update): Boolean {
        return isWellKnown(update).also { wellKnown ->
            if (!wellKnown && !isBlocked(update)) {
                val prettyUpdate = prettyPrint.convert(Update.serializer(), update)
                log.error("Don't process update $prettyUpdate since it's not part of chat family.")

                notificationService.notifyAdmins(
                    buildString {
                        appendLine("*Update from unknown chat received: ${update.update_id}*")
                        appendLine()
                        appendLine("```json")
                        appendLine(prettyUpdate)
                        appendLine("```")
                    }
                )
            }
        }
    }

    private fun isBlocked(update: Update): Boolean {
        val chat = update.message?.chat
            ?: update.edited_message?.chat
            ?: return true
        val isBlocked = config.blockedGroups.contains(chat.id)

        return when (chat.type) {
            "supergroup",
            "channel",
            "group" -> isBlocked

            "private" -> false
            else -> true
        }
    }

    private fun isWellKnown(update: Update): Boolean {
        val chat = update.message?.chat
            ?: update.edited_message?.chat
            ?: return true
        val isWellKnown = knownGroups.contains(chat.id)

        return when (chat.type) {
            "supergroup",
            "channel",
            "group" -> isWellKnown

            "private" -> true
            else -> false
        }
    }

    companion object {
        private val log = logger<KnownChatsFilter>()
    }
}
