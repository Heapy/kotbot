package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a file stored on the Telegram servers. By default, this file will be sent by the user with an optional caption. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the file.
 */
@Serializable
public data class InlineQueryResultCachedDocument(
  /**
   * Type of the result, must be *document*
   */
  public val type: String = "document",
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * Title for the result
   */
  public val title: String,
  /**
   * A valid file identifier for the file
   */
  public val document_file_id: String,
  /**
   * *Optional*. Short description of the result
   */
  public val description: String? = null,
  /**
   * *Optional*. Caption of the document to be sent, 0-1024 characters after entities parsing
   */
  public val caption: String? = null,
  /**
   * *Optional*. Mode for parsing entities in the document caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
   */
  public val parse_mode: String? = null,
  /**
   * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
   */
  public val caption_entities: List<MessageEntity>? = null,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the file
   */
  public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
