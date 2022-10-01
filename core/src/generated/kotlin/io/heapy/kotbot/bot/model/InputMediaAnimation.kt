package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 */
@Serializable
public data class InputMediaAnimation(
  /**
   * Type of the result, must be *animation*
   */
  public val type: String = "animation",
  /**
   * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
   */
  public val media: String,
  /**
   * *Optional*. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
   */
  public val thumb: Thumb? = null,
  /**
   * *Optional*. Caption of the animation to be sent, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the animation caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. Animation width
   */
  public val width: Int? = null,
  /**
   * *Optional*. Animation height
   */
  public val height: Int? = null,
  /**
   * *Optional*. Animation duration in seconds
   */
  public val duration: Int? = null,
) : InputMedia
