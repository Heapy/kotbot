package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.*
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

enum class FamilyCallbacks {
    RefreshAdminPermissions
}

fun reportRule(store: BotStore, state: State) = commandRule("/report", state) { _, message, _ ->
    val chat = message.chat
    val family = store.families
        .firstOrNull { message.chatId in it.chatIds }
        ?: return@commandRule emptyList()
    val chatTitle = "\"${chat.title}\""
    val chatMention = chat.userName?.let { "$chatTitle (@$it)" } ?: chatTitle
    listOf(
        SendMessageAction(
            family.adminChatId,
            "Report sent by @${message.from.userName} in $chatMention"
        )
    )
}

fun refreshPermissionsCallbackRule(state: State) = callbackQueryRule { callback, _ ->
    if(callback.data != FamilyCallbacks.RefreshAdminPermissions.name)
        emptyList()
    else
        // TODO should definitely get family and retry actions only for it
        state.deferredActions.values.flatten()
}

fun familyStartRule(state: State) = commandRule("/start", state) { args, message, queries ->
    val startText = "/start@${state.botUserName}"

    val messageId = message.messageId
    val chatId = message.chat.id
    val chatTitle = "\"${message.chat.title}\""
    val from = message.from
    val text = message.text
    val hash = text.substring(startText.length + 1)
    val family = state.familyAddRequests[hash]

    when {
        family == null -> listOf(
            SendMessageAction(chatId, "The request is expired, please try again!")
        )
        chatId in family.chatIds -> listOf(
            DeleteMessageAction(chatId, messageId),
            SendMessageAction(
                family.adminChatId,
                "Picked chat $chatTitle is already a member " +
                        "of this family, please pick another one")
        )
        !queries.isAdminUser(chatId, from.id) -> listOf(
            SendMessageAction(
                family.adminChatId,
                "Sorry, @${from.userName}, you are not an admin in $chatTitle")
        )
        else -> {
            family.chatIds += chatId
            val deleteAction = DeleteMessageAction(chatId, messageId)
            if(!queries.isAdminUser(chatId, state.botUserId)) {
                state.deferAction(chatId, deleteAction)
                listOf(
                    SendMessageAction(
                        family.adminChatId,
                        "Chat $chatTitle is now a member of the family! Don't forget " +
                                "to give the bot admin rights :)",
                        listOf(
                            listOf(
                                InlineKeyboardButton("Admin rights granted").apply {
                                    callbackData = FamilyCallbacks.RefreshAdminPermissions.name
                                }
                            )
                        )
                    )
                )
            } else {
                listOf(
                    deleteAction,
                    SendMessageAction(
                        family.adminChatId,
                        "Chat $chatTitle is now a member of the family!")
                )
            }
        }
    }
}

fun familyLeaveRule(store: BotStore, state: State) = rule { update, _ ->
    if(!update.hasMessage()) return@rule emptyList()
    val message = update.message
    val chat = message.chat
    val chatId = chat.id
    val leftChatMember = message.leftChatMember
    if(leftChatMember == null || leftChatMember.id != state.botUserId)
        return@rule emptyList()
    val family = store.families
        .firstOrNull { chatId in it.chatIds }
        ?: return@rule emptyList()
    family.chatIds.remove(chatId)
    listOf(
        SendMessageAction(
            family.adminChatId,
            "Chat \"${chat.title}\" left the family as the bot was removed from it :("
        )
    )
}

fun familyCreateRule(store: BotStore, state: State) = adminCommandRule("/family", state) { _, message, queries ->
    val chatId = message.chatId
    when {
        chatId in store.families.map { it.adminChatId } -> listOf(
            SendMessageAction(chatId, "This chat is already bound to the family")
        )
        else -> {
            store.families.add(Family(mutableListOf(), chatId))
            listOf(
                SendMessageAction(chatId,
                    "This chat is now a family admin!\n" +
                            "Use `/family-add` to add other chats to the family")
            )
        }
    }
}

fun familyListRule(store: BotStore, state: State) = adminCommandRule("/family_list", state) { _, message, queries ->
    val chatId = message.chatId
    val family = store.families.firstOrNull { it.adminChatId == chatId }
    if (family == null) {
        listOf(
            SendMessageAction(
                chatId,
                "Didn't find family of this chat :(\n" +
                        "Use `/family` to create a new one!"
            )
        )
    } else {
        listOf(
            SendMessageAction(
                chatId,
                family.chatIds.map(queries::getChatName).joinToString(
                    prefix = "Chats in our family:\n- ",
                    separator = "\n-")
            )
        )
    }
}

fun familyAddRule(store: BotStore, state: State) = adminCommandRule("/family_add", state) { _, message, queries ->
    val chatId = message.chatId
    val family = store.families.firstOrNull { it.adminChatId == chatId }
    if (family == null) {
        listOf(
            SendMessageAction(
                chatId,
                "Didn't find family of this chat :(\n" +
                        "Use `/family` to create a new one!"
            )
        )
    } else {
        val hash = randomString(32)
        state.familyAddRequests[hash] = family
        listOf(
            SendMessageAction(
                chatId,
                "Please pick the chat to add: https://telegram.me/${state.botUserName}?startgroup=$hash\n" +
                        "Note that you must be an admin in that chat!"
                /*listOf(listOf(
                    InlineKeyboardButton("Cancel").apply {
                        callbackData = "..."
                    }
                ))*/
            )
        )
    }
}

fun familyRules(store: BotStore, state: State) = compositeRule(
    reportRule(store, state),
    refreshPermissionsCallbackRule(state),
    familyStartRule(state),
    familyLeaveRule(store, state),

    familyCreateRule(store, state),
    familyListRule(store, state),
    familyAddRule(store, state)
)
