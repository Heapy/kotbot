package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.BanChatMember
import io.heapy.kotbot.bot.method.DeleteMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.User

val TypedUpdate.anyMessage: Message?
    get() = when(this) {
        is TypedMessage -> value
        is TypedEditedMessage -> value
        else -> null
    }

inline fun TypedUpdate.anyText(body: (String, Message) -> Unit) {
    anyMessage?.let { msg ->
        val text = msg.caption.orEmpty() + msg.text.orEmpty()
        if (text.isNotEmpty()) {
            body(text, msg)
        }
    }
}

val User.ref: String get() = if (username != null) "@$username" else "$first_name $last_name (id:$id)"

/**
 * Variant of [User.ref] that includes all fields for logging.
 */
val User.refLog: String get() = "[@$username][$first_name $last_name][#$id]"

val Message.delete: DeleteMessage
    get() = DeleteMessage(
        chat_id = LongChatId(chat.id),
        message_id = message_id,
    )

val Message.banFrom: BanChatMember
    get() = BanChatMember(
        chat_id = LongChatId(chat.id),
        user_id = from!!.id,
    )
