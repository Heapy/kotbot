package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording will be sent by the user. Alternatively, you can use *input\_message\_content* to send a message with the specified content instead of the the voice message.
 */
@Serializable
public data class InlineQueryResultVoice(
  /**
   * Type of the result, must be *voice*
   */
  public val type: String,
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * A valid URL for the voice recording
   */
  public val voice_url: String,
  /**
   * Recording title
   */
  public val title: String,
  /**
   * *Optional*. Caption, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the voice message caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse\_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. Recording duration in seconds
   */
  public val voice_duration: Int? = null,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the voice recording
   */
  public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
