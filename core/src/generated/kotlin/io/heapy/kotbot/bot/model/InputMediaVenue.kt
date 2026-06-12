package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a venue to be sent.
 */
@Serializable
public data class InputMediaVenue(
    /**
     * Type of the result, must be *venue*
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
     * Name of the venue
     */
    public val title: String,
    /**
     * Address of the venue
     */
    public val address: String,
    /**
     * *Optional*. Foursquare identifier of the venue
     */
    public val foursquare_id: String? = null,
    /**
     * *Optional*. Foursquare type of the venue, if known. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
     */
    public val foursquare_type: String? = null,
    /**
     * *Optional*. Google Places identifier of the venue
     */
    public val google_place_id: String? = null,
    /**
     * *Optional*. Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
     */
    public val google_place_type: String? = null,
) : InputPollMedia,
    InputPollOptionMedia
