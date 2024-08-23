package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The background is a PNG or TGV (gzipped subset of SVG with MIME type "application/x-tgwallpattern") pattern to be combined with the background fill chosen by the user.
 */
@Serializable
public data class BackgroundTypePattern(
    /**
     * Type of the background, always "pattern"
     */
    public val type: String,
    /**
     * Document with the pattern
     */
    public val document: Document,
    /**
     * The background fill that is combined with the pattern
     */
    public val fill: BackgroundFill,
    /**
     * Intensity of the pattern when it is shown above the filled background; 0-100
     */
    public val intensity: Int,
    /**
     * *Optional*. *True*, if the background fill must be applied only to the pattern itself. All other pixels are black in this case. For dark themes only
     */
    public val is_inverted: Boolean? = null,
    /**
     * *Optional*. *True*, if the background moves slightly when the device is tilted
     */
    public val is_moving: Boolean? = null,
) : BackgroundType
