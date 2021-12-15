package io.heapy.kotbot

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

public val Update.anyMessage: Message?
    get() = editedMessage ?: message

public inline fun Update.anyText(body: (String, Message) -> Unit) {
    anyMessage?.let { msg ->
        val text = msg.caption.orEmpty() + msg.text.orEmpty()
        if (text.isNotEmpty()) {
            body(text, msg)
        }
    }
}

public val User.info: String get() = "@$userName/id:$id"
