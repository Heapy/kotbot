package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents an animated emoji that displays a random value.
 */
@Serializable
public data class Dice(
    /**
     * Emoji on which the dice throw animation is based
     */
    public val emoji: String,
    /**
     * Value of the dice, 1-6 for "🎲", "🎯" and "🎳" base emoji, 1-5 for "🏀" and "⚽" base emoji, 1-64 for "🎰" base emoji
     */
    public val `value`: Int,
)
