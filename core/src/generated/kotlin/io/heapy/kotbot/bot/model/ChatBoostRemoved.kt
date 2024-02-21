package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a boost removed from a chat.
 */
@Serializable
public data class ChatBoostRemoved(
    /**
     * Chat which was boosted
     */
    public val chat: Chat,
    /**
     * Unique identifier of the boost
     */
    public val boost_id: String,
    /**
     * Point in time (Unix timestamp) when the boost was removed
     */
    public val remove_date: Long,
    /**
     * Source of the removed boost
     */
    public val source: ChatBoostSource,
)
