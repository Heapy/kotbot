package io.heapy.kotbot

import io.heapy.kotbot.bot.BanChatMember
import io.heapy.kotbot.bot.DeleteMessage
import io.heapy.kotbot.bot.Update
import io.heapy.kotbot.bot.User

val Update.anyMessage: Update.Message?
    get() = edited_message ?: message

inline fun Update.anyText(body: (String, Update.Message) -> Unit) {
    anyMessage?.let { msg ->
        val text = msg.caption.orEmpty() + msg.text.orEmpty()
        if (text.isNotEmpty()) {
            body(text, msg)
        }
    }
}

val User.info: String get() = "@$username/id:$id"

val Update.Message.delete: DeleteMessage
    get() = DeleteMessage(chat.id.toString(), message_id)

val Update.Message.banFrom: BanChatMember
    get() = BanChatMember(chat.id.toString(), from!!.id)
