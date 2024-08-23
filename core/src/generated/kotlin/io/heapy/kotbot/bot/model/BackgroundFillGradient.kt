package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The background is a gradient fill.
 */
@Serializable
public data class BackgroundFillGradient(
    /**
     * Type of the background fill, always "gradient"
     */
    public val type: String,
    /**
     * Top color of the gradient in the RGB24 format
     */
    public val top_color: Int,
    /**
     * Bottom color of the gradient in the RGB24 format
     */
    public val bottom_color: Int,
    /**
     * Clockwise rotation angle of the background fill in degrees; 0-359
     */
    public val rotation_angle: Int,
) : BackgroundFill
