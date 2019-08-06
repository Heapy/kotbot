package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.BotStore
import io.heapy.kotbot.bot.State

fun getIdRule(state: State) : Rule = adminCommandRule("/getid", state) { _, message, _ ->
    val chat = message.chat
    val chatId = chat.id
    listOf(
        DeleteMessageAction(chatId, message.messageId),
        SendMessageAction(chatId, "Chat id for \"${chat.title}\" is ${chatId}")
    )
}

fun admRule(store: BotStore, state: State) : Rule = commandRule("/adm", state) { args, message, _ ->
    val admMsg = "@${message.from.userName}: $args"
    val chatId = message.chat.id
    listOf(DeleteMessageAction(chatId, message.messageId)) +
            store.families
                .filter { it.chatIds.contains(chatId) }
                .map { SendMessageAction(it.adminChatId, admMsg) }
}

fun devRules(store: BotStore, state: State) = compositeRule(
    getIdRule(state),
    admRule(store, state)
)
