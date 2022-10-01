package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ReplyMarkup
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to send information about a venue. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendVenue(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Latitude of the venue
   */
  public val latitude: Double,
  /**
   * Longitude of the venue
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
   * Foursquare identifier of the venue
   */
  public val foursquare_id: String? = null,
  /**
   * Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
   */
  public val foursquare_type: String? = null,
  /**
   * Google Places identifier of the venue
   */
  public val google_place_id: String? = null,
  /**
   * Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
   */
  public val google_place_type: String? = null,
  /**
   * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
   */
  public val disable_notification: Boolean? = null,
  /**
   * Protects the contents of the sent message from forwarding and saving
   */
  public val protect_content: Boolean? = null,
  /**
   * If the message is a reply, ID of the original message
   */
  public val reply_to_message_id: Int? = null,
  /**
   * Pass *True* if the message should be sent even if the specified replied-to message is not found
   */
  public val allow_sending_without_reply: Boolean? = null,
  /**
   * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating), [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
   */
  public val reply_markup: ReplyMarkup? = null,
)
