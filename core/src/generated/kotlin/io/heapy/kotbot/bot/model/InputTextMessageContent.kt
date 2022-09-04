package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents the [content](https://core.telegram.org/bots/api/#inputmessagecontent) of a text message to be sent as the result of an inline query.
 */
@Serializable
public data class InputTextMessageContent(
  /**
   * Text of the message to be sent, 1-4096 characters
   */
  public val message_text: String,
  /**
   * *Optional*. Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in message text, which can be specified instead of *parse\_mode*
   */
  public val entities: List<MessageEntity>? = null,
  /**
   * *Optional*. Disables link previews for links in the sent message
   */
  public val disable_web_page_preview: Boolean? = null,
)
