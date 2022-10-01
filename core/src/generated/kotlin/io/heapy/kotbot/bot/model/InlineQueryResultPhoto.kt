package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a photo. By default, this photo will be sent by the user with optional caption. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the photo.
 */
@Serializable
public data class InlineQueryResultPhoto(
  /**
   * Type of the result, must be *photo*
   */
  public val type: String = "photo",
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * A valid URL of the photo. Photo must be in **JPEG** format. Photo size must not exceed 5MB
   */
  public val photo_url: String,
  /**
   * URL of the thumbnail for the photo
   */
  public val thumb_url: String,
  /**
   * *Optional*. Width of the photo
   */
  public val photo_width: Int? = null,
  /**
   * *Optional*. Height of the photo
   */
  public val photo_height: Int? = null,
  /**
   * *Optional*. Title for the result
   */
  public val title: String? = null,
  /**
   * *Optional*. Short description of the result
   */
  public val description: String? = null,
  /**
   * *Optional*. Caption of the photo to be sent, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the photo caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the photo
   */
  public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
