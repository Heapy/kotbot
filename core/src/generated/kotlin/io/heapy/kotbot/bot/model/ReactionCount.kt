package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Represents a reaction added to a message along with the number of times it was added.
 */
@Serializable
public data class ReactionCount(
    /**
     * Type of the reaction
     */
    public val type: ReactionType,
    /**
     * Number of times the reaction was added
     */
    public val total_count: Int,
)
