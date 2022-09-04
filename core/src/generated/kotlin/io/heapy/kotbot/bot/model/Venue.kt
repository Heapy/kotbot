package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a venue.
 */
@Serializable
public data class Venue(
  /**
   * Venue location. Can't be a live location
   */
  public val location: Location,
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
   * *Optional*. Foursquare type of the venue. (For example, “arts\_entertainment/default”, “arts\_entertainment/aquarium” or “food/icecream”.)
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
)
