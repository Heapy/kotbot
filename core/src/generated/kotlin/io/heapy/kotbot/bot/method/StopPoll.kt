package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Use this method to stop a poll which was sent by the bot. On success, the stopped [Poll](https://core.telegram.org/bots/api/#poll) is returned.
 */
@Serializable
public data class StopPoll(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Identifier of the original message with the poll
   */
  public val message_id: Int,
  /**
   * A JSON-serialized object for a new message [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating).
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
)
