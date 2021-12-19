package io.heapy.kotbot

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
