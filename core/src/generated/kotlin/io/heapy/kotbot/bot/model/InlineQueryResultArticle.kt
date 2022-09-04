package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a link to an article or web page.
 */
@Serializable
public data class InlineQueryResultArticle(
  /**
   * Type of the result, must be *article*
   */
  public val type: String,
  /**
   * Unique identifier for this result, 1-64 Bytes
   */
  public val id: String,
  /**
   * Title of the result
   */
  public val title: String,
  /**
   * Content of the message to be sent
   */
  public val input_message_content: InputMessageContent,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. URL of the result
   */
  public val url: String? = null,
  /**
   * *Optional*. Pass *True* if you don't want the URL to be shown in the message
   */
  public val hide_url: Boolean? = null,
  /**
   * *Optional*. Short description of the result
   */
  public val description: String? = null,
  /**
   * *Optional*. Url of the thumbnail for the result
   */
  public val thumb_url: String? = null,
  /**
   * *Optional*. Thumbnail width
   */
  public val thumb_width: Int? = null,
  /**
   * *Optional*. Thumbnail height
   */
  public val thumb_height: Int? = null,
)
