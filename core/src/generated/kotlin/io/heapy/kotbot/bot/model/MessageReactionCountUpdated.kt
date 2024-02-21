package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents reaction changes on a message with anonymous reactions.
 */
@Serializable
public data class MessageReactionCountUpdated(
    /**
     * The chat containing the message
     */
    public val chat: Chat,
    /**
     * Unique message identifier inside the chat
     */
    public val message_id: Int,
    /**
     * Date of the change in Unix time
     */
    public val date: Long,
    /**
     * List of reactions that are present on the message
     */
    public val reactions: List<ReactionCount>,
)
