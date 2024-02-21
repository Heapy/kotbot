package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a change of a reaction on a message performed by a user.
 */
@Serializable
public data class MessageReactionUpdated(
    /**
     * The chat containing the message the user reacted to
     */
    public val chat: Chat,
    /**
     * Unique identifier of the message inside the chat
     */
    public val message_id: Int,
    /**
     * *Optional*. The user that changed the reaction, if the user isn't anonymous
     */
    public val user: User? = null,
    /**
     * *Optional*. The chat on behalf of which the reaction was changed, if the user is anonymous
     */
    public val actor_chat: Chat? = null,
    /**
     * Date of the change in Unix time
     */
    public val date: Long,
    /**
     * Previous list of reaction types that were set by the user
     */
    public val old_reaction: List<ReactionType>,
    /**
     * New list of reaction types that have been set by the user
     */
    public val new_reaction: List<ReactionType>,
)
