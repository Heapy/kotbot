package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object describes the position on faces where a mask should be placed by default.
 */
@Serializable
public data class MaskPosition(
    /**
     * The part of the face relative to which the mask should be placed. One of “forehead”, “eyes”, “mouth”, or “chin”.
     */
    public val point: String,
    /**
     * Shift by X-axis measured in widths of the mask scaled to the face size, from left to right. For example, choosing -1.0 will place mask just to the left of the default mask position.
     */
    public val x_shift: Double,
    /**
     * Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom. For example, 1.0 will place the mask just below the default mask position.
     */
    public val y_shift: Double,
    /**
     * Mask scaling coefficient. For example, 2.0 means double size.
     */
    public val scale: Double,
)
