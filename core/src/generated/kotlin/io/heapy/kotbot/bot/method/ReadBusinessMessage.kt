package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Marks incoming message as read on behalf of a business account. Requires the *can_read_messages* business bot right. Returns *True* on success.
 */
@Serializable
public data class ReadBusinessMessage(
    /**
     * Unique identifier of the business connection on behalf of which to read the message
     */
    public val business_connection_id: String,
    /**
     * Unique identifier of the chat in which the message was received. The chat must have been active in the last 24 hours.
     */
    public val chat_id: Long,
    /**
     * Unique identifier of the message to mark as read
     */
    public val message_id: Int,
) : Method<ReadBusinessMessage, Boolean> by Companion {
    public companion object : Method<ReadBusinessMessage, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<ReadBusinessMessage> = serializer()

        override val _name: String = "readBusinessMessage"
    }
}
