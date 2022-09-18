package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use *input\_message\_content* to send a message with the specified content instead of the video.
 *
 * If an InlineQueryResultVideo message contains an embedded video (e.g., YouTube), you **must** replace its content using *input\_message\_content*.
 */
@Serializable
public data class InlineQueryResultVideo(
  /**
   * Type of the result, must be *video*
   */
  public val type: String,
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * A valid URL for the embedded video player or video file
   */
  public val video_url: String,
  /**
   * MIME type of the content of the video URL, “text/html” or “video/mp4”
   */
  public val mime_type: String,
  /**
   * URL of the thumbnail (JPEG only) for the video
   */
  public val thumb_url: String,
  /**
   * Title for the result
   */
  public val title: String,
  /**
   * *Optional*. Caption of the video to be sent, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the video caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse\_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. Video width
   */
  public val video_width: Int? = null,
  /**
   * *Optional*. Video height
   */
  public val video_height: Int? = null,
  /**
   * *Optional*. Video duration in seconds
   */
  public val video_duration: Int? = null,
  /**
   * *Optional*. Short description of the result
   */
  public val description: String? = null,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the video. This field is **required** if InlineQueryResultVideo is used to send an HTML-page as a result (e.g., a YouTube video).
   */
  public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
