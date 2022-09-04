package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Represents the [content](https://core.telegram.org/bots/api/#inputmessagecontent) of a location message to be sent as the result of an inline query.
 */
@Serializable
public data class InputLocationMessageContent(
  /**
   * Latitude of the location in degrees
   */
  public val latitude: Double,
  /**
   * Longitude of the location in degrees
   */
  public val longitude: Double,
  /**
   * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
   */
  public val horizontal_accuracy: Double? = null,
  /**
   * *Optional*. Period in seconds for which the location can be updated, should be between 60 and 86400.
   */
  public val live_period: Int? = null,
  /**
   * *Optional*. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
   */
  public val heading: Int? = null,
  /**
   * *Optional*. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
   */
  public val proximity_alert_radius: Int? = null,
)
