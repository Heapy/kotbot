package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the sticker.
 */
@Serializable
public data class InlineQueryResultCachedSticker(
  /**
   * Type of the result, must be *sticker*
   */
  public val type: String = "sticker",
  /**
   * Unique identifier for this result, 1-64 bytes
   */
  public val id: String,
  /**
   * A valid file identifier of the sticker
   */
  public val sticker_file_id: String,
  /**
   * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
  /**
   * *Optional*. Content of the message to be sent instead of the sticker
   */
  public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
