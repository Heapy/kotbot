package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to send a game. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendGame(
  /**
   * Unique identifier for the target chat
   */
  public val chat_id: Long,
  /**
   * Short name of the game, serves as the unique identifier for the game. Set up your games via [@BotFather](https://t.me/botfather).
   */
  public val game_short_name: String,
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
   * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating). If empty, one 'Play game_title' button will be shown. If not empty, the first button must launch the game.
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
)
