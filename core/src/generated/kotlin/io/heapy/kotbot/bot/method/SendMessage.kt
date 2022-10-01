package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.ReplyMarkup
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Use this method to send text messages. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendMessage(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Text of the message to be sent, 1-4096 characters after entities parsing
   */
  public val text: String,
  /**
   * Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
   */
  public val entities: List<MessageEntity>? = null,
  /**
   * Disables link previews for links in this message
   */
  public val disable_web_page_preview: Boolean? = null,
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
