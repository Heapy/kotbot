package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a point on the map.
 */
@Serializable
public data class Location(
    /**
     * Longitude as defined by sender
     */
    public val longitude: Double,
    /**
     * Latitude as defined by sender
     */
    public val latitude: Double,
    /**
     * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
     */
    public val horizontal_accuracy: Double? = null,
    /**
     * *Optional*. Time relative to the message sending date, during which the location can be updated; in seconds. For active live locations only.
     */
    public val live_period: Int? = null,
    /**
     * *Optional*. The direction in which user is moving, in degrees; 1-360. For active live locations only.
     */
    public val heading: Int? = null,
    /**
     * *Optional*. The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only.
     */
    public val proximity_alert_radius: Int? = null,
)
