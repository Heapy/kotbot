package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to an MP3 audio file. By default, this audio file will be sent by the user. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the audio.
 */
@Serializable
public data class InlineQueryResultAudio(
  /**
   * Type of the result, must be *audio*
   */
  public val type: String = "audio",
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * A valid URL for the audio file
   */
  public val audio_url: String,
  /**
   * Title
   */
  public val title: String,
  /**
   * *Optional*. Caption, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the audio caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. Performer
   */
  public val performer: String? = null,
  /**
   * *Optional*. Audio duration in seconds
   */
  public val audio_duration: Int? = null,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the audio
   */
  public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
