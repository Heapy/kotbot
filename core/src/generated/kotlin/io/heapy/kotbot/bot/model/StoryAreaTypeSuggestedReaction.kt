package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a story area pointing to a suggested reaction. Currently, a story can have up to 5 suggested reaction areas.
 */
@Serializable
public data class StoryAreaTypeSuggestedReaction(
    /**
     * Type of the area, always "suggested_reaction"
     */
    public val type: String,
    /**
     * Type of the reaction
     */
    public val reaction_type: ReactionType,
    /**
     * *Optional*. Pass *True* if the reaction area has a dark background
     */
    public val is_dark: Boolean? = null,
    /**
     * *Optional*. Pass *True* if reaction area corner is flipped
     */
    public val is_flipped: Boolean? = null,
) : StoryAreaType
