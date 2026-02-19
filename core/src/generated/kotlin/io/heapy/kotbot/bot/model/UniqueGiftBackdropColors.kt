package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object describes the colors of the backdrop of a unique gift.
 */
@Serializable
public data class UniqueGiftBackdropColors(
    /**
     * The color in the center of the backdrop in RGB format
     */
    public val center_color: Int,
    /**
     * The color on the edges of the backdrop in RGB format
     */
    public val edge_color: Int,
    /**
     * The color to be applied to the symbol in RGB format
     */
    public val symbol_color: Int,
    /**
     * The color for the text on the backdrop in RGB format
     */
    public val text_color: Int,
)
