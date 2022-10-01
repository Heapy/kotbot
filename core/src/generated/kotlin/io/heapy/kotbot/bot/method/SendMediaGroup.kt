package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InputMedia
import kotlin.Boolean
import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of [Messages](https://core.telegram.org/bots/api/#message) that were sent is returned.
 */
@Serializable
public data class SendMediaGroup(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * A JSON-serialized array describing messages to be sent, must include 2-10 items
   */
  public val media: List<InputMedia>,
  /**
   * Sends messages [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
   */
  public val disable_notification: Boolean? = null,
  /**
   * Protects the contents of the sent messages from forwarding and saving
   */
  public val protect_content: Boolean? = null,
  /**
   * If the messages are a reply, ID of the original message
   */
  public val reply_to_message_id: Int? = null,
  /**
   * Pass *True* if the message should be sent even if the specified replied-to message is not found
   */
  public val allow_sending_without_reply: Boolean? = null,
)
