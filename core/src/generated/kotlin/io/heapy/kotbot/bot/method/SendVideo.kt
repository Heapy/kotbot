package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.ReplyMarkup
import io.heapy.kotbot.bot.model.Thumb
import io.heapy.kotbot.bot.model.Video
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as [Document](https://core.telegram.org/bots/api/#document)). On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
 */
@Serializable
public data class SendVideo(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
   */
  public val video: Video,
  /**
   * Duration of sent video in seconds
   */
  public val duration: Int? = null,
  /**
   * Video width
   */
  public val width: Int? = null,
  /**
   * Video height
   */
  public val height: Int? = null,
  /**
   * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
   */
  public val thumb: Thumb? = null,
  /**
   * Video caption (may also be used when resending videos by *file_id*), 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * Mode for parsing entities in the video caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * Pass *True* if the uploaded video is suitable for streaming
   */
  public val supports_streaming: Boolean? = null,
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
