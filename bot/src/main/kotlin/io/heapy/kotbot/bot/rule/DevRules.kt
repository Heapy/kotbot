package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.BotStore
import org.telegram.telegrambots.meta.api.objects.Update

class GetIdRule(private val store: BotStore) : Rule {
    override fun validate(update: Update): List<Action> {
        if(!update.hasMessage()) return emptyList()
        val message = update.message
        if(!message.hasText()) return emptyList()
        val text = message.text
        val chat = message.chat
        return when {
            text == "/getid" -> {
                listOf(
                    DeleteMessageAction(chat.id, message.messageId),
                    SendMessageAction(chat.id, "Chat id for \"${chat.title}\" is ${chat.id}")
                )
            }
            text.startsWith("/adm ") -> {
                val admMsg = "@${message.from.userName}: ${text.substring("/adm ".length)}"
                listOf(DeleteMessageAction(chat.id, message.messageId)) +
                    store.families
                        .filter { it.chatIds.contains(chat.id) }
                        .map { SendMessageAction(it.adminChatId, admMsg) }
            }
            else -> emptyList()
        }
    }
}

