package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The reaction is based on a custom emoji.
 */
@Serializable
public data class ReactionTypeCustomEmoji(
    /**
     * Type of the reaction, always "custom_emoji"
     */
    public val type: String,
    /**
     * Custom emoji identifier
     */
    public val custom_emoji_id: String,
) : ReactionType
