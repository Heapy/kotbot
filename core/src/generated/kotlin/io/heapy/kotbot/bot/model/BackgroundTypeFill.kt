package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The background is automatically filled based on the selected colors.
 */
@Serializable
public data class BackgroundTypeFill(
    /**
     * Type of the background, always "fill"
     */
    public val type: String,
    /**
     * The background fill
     */
    public val fill: BackgroundFill,
    /**
     * Dimming of the background in dark themes, as a percentage; 0-100
     */
    public val dark_theme_dimming: Int,
) : BackgroundType
