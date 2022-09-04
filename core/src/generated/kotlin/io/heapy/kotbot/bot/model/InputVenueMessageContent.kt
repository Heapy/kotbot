package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the [content](https://core.telegram.org/bots/api/#inputmessagecontent) of a venue message to be sent as the result of an inline query.
 */
@Serializable
public data class InputVenueMessageContent(
  /**
   * Latitude of the venue in degrees
   */
  public val latitude: Double,
  /**
   * Longitude of the venue in degrees
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
   * *Optional*. Foursquare identifier of the venue, if known
   */
  public val foursquare_id: String? = null,
  /**
   * *Optional*. Foursquare type of the venue, if known. (For example, “arts\_entertainment/default”, “arts\_entertainment/aquarium” or “food/icecream”.)
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
