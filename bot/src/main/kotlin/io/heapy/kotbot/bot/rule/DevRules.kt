package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.BotStore
import io.heapy.kotbot.bot.State

/**
 * Command `/getid` (admin): responds with current chat id.
 */
suspend fun getIdRule(state: State) : Rule = adminCommandRule("/getid", state) { _, message, _ ->
    val chat = message.chat
    val chatId = chat.id
    listOf(
        DeleteMessageAction(chatId, message.messageId),
        SendMessageAction(chatId, "Chat id for \"${chat.title}\" is $chatId")
    )
}

/**
 * Command `/adm message`: sends the message to family administrator chat.
 */
suspend fun admRule(store: BotStore, state: State) : Rule = commandRule("/adm", state) { args, message, _ ->
    val admMsg = "@${message.from.userName}: $args"
    val chatId = message.chat.id
    listOf(DeleteMessageAction(chatId, message.messageId)) +
            store.families
                .filter { it.chatIds.contains(chatId) }
                .map { SendMessageAction(it.adminChatId, admMsg) }
}

/**
 * A group of commands useful for development purposes.
 */
suspend fun devRules(store: BotStore, state: State) = compositeRule(
    getIdRule(state),
    admRule(store, state)
)
