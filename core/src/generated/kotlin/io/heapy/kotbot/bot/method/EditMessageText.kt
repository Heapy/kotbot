package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.MessageEntity
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Use this method to edit text and [game](https://core.telegram.org/bots/api/#games) messages. On success, if the edited message is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned.
 */
@Serializable
public data class EditMessageText(
  /**
   * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId? = null,
  /**
   * Required if *inline_message_id* is not specified. Identifier of the message to edit
   */
  public val message_id: Int? = null,
  /**
   * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
   */
  public val inline_message_id: String? = null,
  /**
   * New text of the message, 1-4096 characters after entities parsing
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
   * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating).
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
)
