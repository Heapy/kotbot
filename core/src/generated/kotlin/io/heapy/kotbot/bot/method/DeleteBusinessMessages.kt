package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Delete messages on behalf of a business account. Requires the *can_delete_sent_messages* business bot right to delete messages sent by the bot itself, or the *can_delete_all_messages* business bot right to delete any message. Returns *True* on success.
 */
@Serializable
public data class DeleteBusinessMessages(
    /**
     * Unique identifier of the business connection on behalf of which to delete the messages
     */
    public val business_connection_id: String,
    /**
     * A JSON-serialized list of 1-100 identifiers of messages to delete. All messages must be from the same chat. See [deleteMessage](https://core.telegram.org/bots/api/#deletemessage) for limitations on which messages can be deleted
     */
    public val message_ids: List<Int>,
) : Method<DeleteBusinessMessages, Boolean> by Companion {
    public companion object : Method<DeleteBusinessMessages, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteBusinessMessages> = serializer()

        override val _name: String = "deleteBusinessMessages"
    }
}
