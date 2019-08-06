package io.heapy.kotbot.bot.rule

import io.heapy.kotbot.bot.*
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

enum class FamilyCallbacks {
    RefreshAdminPermissions
}

class ReportRule(private val store: BotStore) : Rule {
    override fun validate(update: Update): List<Action> {
        if(!update.hasMessage()) return emptyList()
        val message = update.message
        if(!message.hasText()) return emptyList()
        if(!message.text.startsWith("/report")) return emptyList()
        val chat = message.chat
        val family = store.families.firstOrNull { message.chatId in it.chatIds }
        if(family == null) return emptyList()
        val chatTitle = "\"${chat.title}\""
        val chatMention = chat.userName?.let { "$chatTitle (@$it)" } ?: chatTitle
        return listOf(
            SendMessageAction(family.adminChatId, "Report sent by @${message.from.userName} in $chatMention")
        )
    }
}

class RefreshPermissionsCallbackRule(private val state: State) : Rule {
    override fun validate(update: Update, queries: BotQueries): List<Action> {
        if(!update.hasCallbackQuery()) return emptyList()
        val callback = update.callbackQuery
        if(callback.data != FamilyCallbacks.RefreshAdminPermissions.name) return emptyList()
        // TODO should definitely get family and retry actions only for it
        return state.deferredActions.values.flatten()
    }
}

class FamilyLeaveRule(private val store: BotStore, private val state: State) : Rule {
    override fun validate(update: Update): List<Action> {
        if(!update.hasMessage()) return emptyList()
        val message = update.message
        val chat = message.chat
        val chatId = chat.id
        val leftChatMember = message.leftChatMember
        if(leftChatMember == null || leftChatMember.id != state.botUserId) return emptyList()
        val family = store.families.firstOrNull { chatId in it.chatIds }
        if(family == null) return emptyList()
        family.chatIds.remove(chatId)
        return listOf(
            SendMessageAction(
                family.adminChatId,
                "Chat \"${chat.title}\" left the family as the bot was removed from it :(")
        )
    }
}

class FamilyStartRule(private val state: State) : Rule {
    private val startText by lazy { "/start@${state.botUserName}" }

    override fun validate(update: Update, queries: BotQueries): List<Action> {
        if(!update.hasMessage()) return emptyList()
        val message = update.message
        if(!message.hasText()) return emptyList()
        val messageId = message.messageId
        val chatId = message.chat.id
        val chatTitle = "\"${message.chat.title}\""
        val from = message.from
        val text = message.text
        return when {
            text.startsWith(startText) -> {
                val hash = text.substring(startText.length + 1)
                val family = state.familyAddRequests[hash]
                when {
                    family == null -> listOf(SendMessageAction(chatId,
                        "The request is expired, please try again!"))
                    chatId in family.chatIds -> listOf(
                        DeleteMessageAction(chatId, messageId),
                        SendMessageAction(family.adminChatId,
                            "Picked chat $chatTitle is already a member " +
                                    "of this family, please pick another one"))
                    !queries.isAdminUser(chatId, from.id) -> listOf(SendMessageAction(family.adminChatId,
                        "Sorry, @${from.userName}, you are not an admin in $chatTitle"))
                    else -> {
                        family.chatIds += chatId
                        val deleteAction = DeleteMessageAction(chatId, messageId)
                        if(!queries.isAdminUser(chatId, state.botUserId)) {
                            state.deferAction(chatId, deleteAction)
                            listOf(SendMessageAction(
                                family.adminChatId,
                                "Chat $chatTitle is now a member of the family! Don't forget " +
                                        "to give the bot admin rights :)",
                                listOf(listOf(
                                    InlineKeyboardButton("Admin rights granted").apply {
                                        callbackData = FamilyCallbacks.RefreshAdminPermissions.name
                                    }
                                )))
                            )
                        } else {
                            listOf(
                                deleteAction,
                                SendMessageAction(family.adminChatId,
                                    "Chat $chatTitle is now a member of the family!"))
                        }
                    }
                }
            }
            else -> emptyList()
        }
    }
}

class FamilyManageRule(private val store: BotStore, private val state: State) : Rule {
    override fun validate(update: Update, queries: BotQueries): List<Action> {
        if(!update.hasMessage()) return emptyList()
        val message = update.message
        if(!message.hasText()) return emptyList()
        val chatId = message.chatId
        val isAdmin = queries.isAdminUser(chatId, message.from.id)
        return when(message.text) {
            "/family" -> {
                when {
                    !isAdmin -> listOf(DeleteMessageAction(chatId, message.messageId))
                    chatId in store.families.map { it.adminChatId } ->
                        listOf(SendMessageAction(chatId, "This chat is already bound to the family"))
                    else -> {
                        store.families.add(Family(mutableListOf(), chatId))
                        listOf(SendMessageAction(chatId,
                            "This chat is now a family admin!\n" +
                                    "Use `/family-add` to add other chats to the family"))
                    }
                }
            }
            "/family-list" -> {
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
            "/family-add" -> {
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
                        DeleteMessageAction(chatId, message.messageId),
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
            else -> emptyList()
        }
    }
}
