package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The message was originally sent to a channel chat.
 */
@Serializable
public data class MessageOriginChannel(
    /**
     * Type of the message origin, always "channel"
     */
    public val type: String = "channel",
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * Channel chat to which the message was originally sent
     */
    public val chat: Chat,
    /**
     * Unique message identifier inside the chat
     */
    public val message_id: Int,
    /**
     * *Optional*. Signature of the original post author
     */
    public val author_signature: String? = null,
) : MessageOrigin
