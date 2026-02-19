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
 * Use this method to approve a suggested post in a direct messages chat. The bot must have the 'can_post_messages' administrator right in the corresponding channel chat. Returns *True* on success.
 */
@Serializable
public data class ApproveSuggestedPost(
    /**
     * Unique identifier for the target direct messages chat
     */
    public val chat_id: Long,
    /**
     * Identifier of a suggested post message to approve
     */
    public val message_id: Int,
    /**
     * Point in time (Unix timestamp) when the post is expected to be published; omit if the date has already been specified when the suggested post was created. If specified, then the date must be not more than 2678400 seconds (30 days) in the future
     */
    public val send_date: Long? = null,
) : Method<ApproveSuggestedPost, Boolean> by Companion {
    public companion object : Method<ApproveSuggestedPost, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<ApproveSuggestedPost> = serializer()

        override val _name: String = "approveSuggestedPost"
    }
}
