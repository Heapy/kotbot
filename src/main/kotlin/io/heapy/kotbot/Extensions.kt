package io.heapy.kotbot

import io.heapy.kotbot.bot.ApiUpdate
import io.heapy.kotbot.bot.User

val ApiUpdate.anyMessage: ApiUpdate.Message?
    get() = edited_message ?: message

inline fun ApiUpdate.anyText(body: (String, ApiUpdate.Message) -> Unit) {
    anyMessage?.let { msg ->
        val text = msg.caption.orEmpty() + msg.text.orEmpty()
        if (text.isNotEmpty()) {
            body(text, msg)
        }
    }
}

val User.info: String get() = "@$username/id:$id"
