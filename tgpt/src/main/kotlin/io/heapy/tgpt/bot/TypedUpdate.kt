package io.heapy.tgpt.bot

import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.model.Message
import kotlinx.serialization.Serializable

@Serializable
sealed interface TypedUpdate {
    val updateId: Int
}

@Serializable
data class TypedMessage(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedEditedMessage(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

fun Update.toTypedUpdate(): TypedUpdate? {
    val message = message
    val edited_message = edited_message

    return when {
        message != null -> TypedMessage(update_id, message)
        edited_message != null -> TypedEditedMessage(update_id, edited_message)
        else -> null
    }
}
