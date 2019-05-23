package io.heapy.kotbot.bot

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

val Update.anyMessage: Message?
    get() = editedMessage ?: message

inline fun Update.anyText(body: (String, Message) -> Unit) {
    anyMessage?.let { msg ->
        val text = msg.caption.orEmpty() + msg.text.orEmpty()
        if (text.isNotEmpty()) {
            body(text, msg)
        }
    }
}
