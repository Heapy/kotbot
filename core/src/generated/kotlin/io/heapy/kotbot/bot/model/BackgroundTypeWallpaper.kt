package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The background is a wallpaper in the JPEG format.
 */
@Serializable
public data class BackgroundTypeWallpaper(
    /**
     * Type of the background, always "wallpaper"
     */
    public val type: String = "wallpaper",
    /**
     * Document with the wallpaper
     */
    public val document: Document,
    /**
     * Dimming of the background in dark themes, as a percentage; 0-100
     */
    public val dark_theme_dimming: Int,
    /**
     * *Optional*. *True*, if the wallpaper is downscaled to fit in a 450x450 square and then box-blurred with radius 12
     */
    public val is_blurred: Boolean? = true,
    /**
     * *Optional*. *True*, if the background moves slightly when the device is tilted
     */
    public val is_moving: Boolean? = true,
) : BackgroundType
