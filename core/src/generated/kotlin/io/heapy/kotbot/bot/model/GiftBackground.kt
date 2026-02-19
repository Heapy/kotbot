package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object describes the background of a gift.
 */
@Serializable
public data class GiftBackground(
    /**
     * Center color of the background in RGB format
     */
    public val center_color: Int,
    /**
     * Edge color of the background in RGB format
     */
    public val edge_color: Int,
    /**
     * Text color of the background in RGB format
     */
    public val text_color: Int,
)
