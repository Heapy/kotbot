package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.BanChatMember
import io.heapy.kotbot.bot.method.DeleteMessage
import io.heapy.kotbot.bot.model.LongChatId
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
    get() = DeleteMessage(
        chat_id = LongChatId(chat.id),
        message_id = message_id,
    )

val Message.banFrom: BanChatMember
    get() = BanChatMember(
        chat_id = LongChatId(chat.id),
        user_id = from!!.id,
    )
