package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.InputChecklist
import io.heapy.kotbot.bot.model.Message
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to edit a checklist on behalf of a connected business account. On success, the edited [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class EditMessageChecklist(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    public val business_connection_id: String,
    /**
     * Unique identifier for the target chat
     */
    public val chat_id: Long,
    /**
     * Unique identifier for the target message
     */
    public val message_id: Int,
    /**
     * A JSON-serialized object for the new checklist
     */
    public val checklist: InputChecklist,
    /**
     * A JSON-serialized object for the new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) for the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<EditMessageChecklist, Message> by Companion {
    public companion object : Method<EditMessageChecklist, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<EditMessageChecklist> = serializer()

        override val _name: String = "editMessageChecklist"
    }
}
