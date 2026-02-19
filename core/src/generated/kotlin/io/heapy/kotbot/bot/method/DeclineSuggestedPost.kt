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
 * Use this method to decline a suggested post in a direct messages chat. The bot must have the 'can_manage_direct_messages' administrator right in the corresponding channel chat. Returns *True* on success.
 */
@Serializable
public data class DeclineSuggestedPost(
    /**
     * Unique identifier for the target direct messages chat
     */
    public val chat_id: Long,
    /**
     * Identifier of a suggested post message to decline
     */
    public val message_id: Int,
    /**
     * Comment for the creator of the suggested post; 0-128 characters
     */
    public val comment: String? = null,
) : Method<DeclineSuggestedPost, Boolean> by Companion {
    public companion object : Method<DeclineSuggestedPost, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeclineSuggestedPost> = serializer()

        override val _name: String = "declineSuggestedPost"
    }
}
