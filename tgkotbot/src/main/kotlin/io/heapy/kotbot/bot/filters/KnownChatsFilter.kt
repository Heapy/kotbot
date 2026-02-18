package io.heapy.kotbot.bot.filters

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.TypedChannelPost
import io.heapy.kotbot.bot.TypedEditedMessage
import io.heapy.kotbot.bot.TypedMessage
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.LeaveChat
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration
import io.heapy.kotbot.infra.debug.PrettyPrint

class KnownChatsFilter(
    private val config: KnownChatsConfiguration,
    private val notificationService: NotificationService,
    private val prettyPrint: PrettyPrint,
    private val kotbot: Kotbot,
) : Filter {
    override suspend fun predicate(
        update: TypedUpdate,
    ): Boolean {
        return isWellKnown(update).also { wellKnown ->
            if (!wellKnown) {
                val chatId = when(update) {
                    is TypedMessage -> update.value.chat.id
                    is TypedEditedMessage -> update.value.chat.id
                    is TypedChannelPost -> update.value.chat.id
                    else -> null
                }

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

                val prettyUpdate = prettyPrint.convert(TypedUpdate.serializer(), update)
                log.error("Don't process update $prettyUpdate since it's not part of chat family.")

                notificationService.notifyAdmins(
                    buildString {
                        appendLine("*Update from unknown chat received: ${update.updateId}*")
                        appendLine()
                        appendLine("```json")
                        appendLine(prettyUpdate)
                        appendLine("```")
                    }
                )
            }
        }
    }

    private fun isWellKnown(
        update: TypedUpdate,
    ): Boolean {
        val chat = when(update) {
            is TypedMessage -> update.value.chat
            is TypedEditedMessage -> update.value.chat
            is TypedChannelPost -> update.value.chat
            else -> return true
        }
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
