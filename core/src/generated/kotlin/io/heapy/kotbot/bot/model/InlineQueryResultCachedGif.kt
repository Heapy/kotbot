package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will be sent by the user with an optional caption. Alternatively, you can use *input\_message\_content* to send a message with specified content instead of the animation.
 */
@Serializable
public data class InlineQueryResultCachedGif(
  /**
   * Type of the result, must be *gif*
   */
  public val type: String,
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * A valid file identifier for the GIF file
   */
  public val gif_file_id: String,
  /**
   * *Optional*. Title for the result
   */
  public val title: String? = null,
  /**
   * *Optional*. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse\_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the GIF animation
   */
  public val input_message_content: InputMessageContent? = null,
)
