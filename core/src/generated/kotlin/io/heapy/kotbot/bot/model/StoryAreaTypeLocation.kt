package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a story area pointing to a location. Currently, a story can have up to 10 location areas.
 */
@Serializable
public data class StoryAreaTypeLocation(
    /**
     * Type of the area, always "location"
     */
    public val type: String,
    /**
     * Location latitude in degrees
     */
    public val latitude: Double,
    /**
     * Location longitude in degrees
     */
    public val longitude: Double,
    /**
     * *Optional*. Address of the location
     */
    public val address: LocationAddress? = null,
) : StoryAreaType
