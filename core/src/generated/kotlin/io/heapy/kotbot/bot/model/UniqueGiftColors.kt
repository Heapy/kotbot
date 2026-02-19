package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about the color scheme for a user's name, message replies and link previews based on a unique gift.
 */
@Serializable
public data class UniqueGiftColors(
    /**
     * Custom emoji identifier of the unique gift's model
     */
    public val model_custom_emoji_id: String,
    /**
     * Custom emoji identifier of the unique gift's symbol
     */
    public val symbol_custom_emoji_id: String,
    /**
     * Main color used in light themes; RGB format
     */
    public val light_theme_main_color: Int,
    /**
     * List of 1-3 additional colors used in light themes; RGB format
     */
    public val light_theme_other_colors: List<Int>,
    /**
     * Main color used in dark themes; RGB format
     */
    public val dark_theme_main_color: Int,
    /**
     * List of 1-3 additional colors used in dark themes; RGB format
     */
    public val dark_theme_other_colors: List<Int>,
)
