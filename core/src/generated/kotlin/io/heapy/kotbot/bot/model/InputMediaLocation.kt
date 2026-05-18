package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a location to be sent.
 */
@Serializable
public data class InputMediaLocation(
    /**
     * Type of the result, must be *location*
     */
    public val type: String,
    /**
     * Latitude of the location
     */
    public val latitude: Double,
    /**
     * Longitude of the location
     */
    public val longitude: Double,
    /**
     * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
     */
    public val horizontal_accuracy: Double? = null,
) : InputPollMedia, InputPollOptionMedia
