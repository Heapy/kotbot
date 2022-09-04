package io.heapy.kotbot

import io.heapy.kotbot.bot.BanChatMember
import io.heapy.kotbot.bot.DeleteMessage
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.model.User

val Update.anyMessage: Message?
    get() = edited_message ?: message

inline fun Update.anyText(body: (String, Message) -> Unit) {
    anyMessage?.let { msg ->
        val text = msg.caption.orEmpty() + msg.text.orEmpty()
        if (text.isNotEmpty()) {
            body(text, msg)
        }
    }
}

val User.info: String get() = "@$username/id:$id"

val Message.delete: DeleteMessage
    get() = DeleteMessage(chat.id.toString(), message_id)

val Message.banFrom: BanChatMember
    get() = BanChatMember(chat.id.toString(), from!!.id)
