package io.heapy.kotbot.bot.filters

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.LeaveChat
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration
import io.heapy.kotbot.infra.debug.PrettyPrint

class KnownChatsFilter(
    private val config: KnownChatsConfiguration,
    private val notificationService: NotificationService,
    private val prettyPrint: PrettyPrint,
    private val kotbot: Kotbot,
) : Filter {
    override suspend fun predicate(update: Update): Boolean {
        return isWellKnown(update).also { wellKnown ->
            if (!wellKnown) {
                val chatId = update.message?.chat?.id
                    ?: update.edited_message?.chat?.id
                    ?: update.channel_post?.chat?.id

                if (chatId != null) {
                    notificationService.notifyAdmins(
                        "Leaving unknown chat $chatId"
                    )
                    kotbot.executeSafely(
                        LeaveChat(
                            chat_id = LongChatId(chatId)
                        )
                    )
                }

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

    private fun isWellKnown(update: Update): Boolean {
        val chat = update.message?.chat
            ?: update.edited_message?.chat
            ?: return true
        val isWellKnown = config.allowedGroups.values.contains(chat.id)

        return when (chat.type) {
            "supergroup",
            "channel",
            "group" -> isWellKnown

            "private" -> true
            else -> false
        }
    }

    private companion object : Logger()
}
