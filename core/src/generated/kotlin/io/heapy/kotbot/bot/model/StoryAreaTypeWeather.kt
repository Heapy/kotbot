package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a story area containing weather information. Currently, a story can have up to 3 weather areas.
 */
@Serializable
public data class StoryAreaTypeWeather(
    /**
     * Type of the area, always "weather"
     */
    public val type: String,
    /**
     * Temperature, in degree Celsius
     */
    public val temperature: Double,
    /**
     * Emoji representing the weather
     */
    public val emoji: String,
    /**
     * A color of the area background in the ARGB format
     */
    public val background_color: Int,
) : StoryAreaType
