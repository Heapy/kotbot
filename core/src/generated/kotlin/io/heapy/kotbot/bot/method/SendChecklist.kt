package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.InputChecklist
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ReplyParameters
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to send a checklist on behalf of a connected business account. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendChecklist(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    public val business_connection_id: String,
    /**
     * Unique identifier for the target chat
     */
    public val chat_id: Long,
    /**
     * A JSON-serialized object for the checklist to send
     */
    public val checklist: InputChecklist,
    /**
     * Sends the message silently. Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message
     */
    public val message_effect_id: String? = null,
    /**
     * A JSON-serialized object for description of the message to reply to
     */
    public val reply_parameters: ReplyParameters? = null,
    /**
     * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<SendChecklist, Message> by Companion {
    public companion object : Method<SendChecklist, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<SendChecklist> = serializer()

        override val _name: String = "sendChecklist"
    }
}
