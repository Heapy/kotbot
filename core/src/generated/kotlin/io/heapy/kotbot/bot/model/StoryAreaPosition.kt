package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlinx.serialization.Serializable

/**
 * Describes the position of a clickable area within a story.
 */
@Serializable
public data class StoryAreaPosition(
    /**
     * The abscissa of the area's center, as a percentage of the media width
     */
    public val x_percentage: Double,
    /**
     * The ordinate of the area's center, as a percentage of the media height
     */
    public val y_percentage: Double,
    /**
     * The width of the area's rectangle, as a percentage of the media width
     */
    public val width_percentage: Double,
    /**
     * The height of the area's rectangle, as a percentage of the media height
     */
    public val height_percentage: Double,
    /**
     * The clockwise rotation angle of the rectangle, in degrees; 0-360
     */
    public val rotation_angle: Double,
    /**
     * The radius of the rectangle corner rounding, as a percentage of the media width
     */
    public val corner_radius_percentage: Double,
)
