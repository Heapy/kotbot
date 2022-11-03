package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a [custom keyboard](https://core.telegram.org/bots/features#keyboards) with reply options (see [Introduction to bots](https://core.telegram.org/bots/features#keyboards) for details and examples).
 */
@Serializable
public data class ReplyKeyboardMarkup(
  /**
   * Array of button rows, each represented by an Array of [KeyboardButton](https://core.telegram.org/bots/api/#keyboardbutton) objects
   */
  public val keyboard: List<List<KeyboardButton>>,
  /**
   * *Optional*. Requests clients to resize the keyboard vertically for optimal fit (e.g., make the keyboard smaller if there are just two rows of buttons). Defaults to *false*, in which case the custom keyboard is always of the same height as the app's standard keyboard.
   */
  public val resize_keyboard: Boolean? = false,
  /**
   * *Optional*. Requests clients to hide the keyboard as soon as it's been used. The keyboard will still be available, but clients will automatically display the usual letter-keyboard in the chat - the user can press a special button in the input field to see the custom keyboard again. Defaults to *false*.
   */
  public val one_time_keyboard: Boolean? = false,
  /**
   * *Optional*. The placeholder to be shown in the input field when the keyboard is active; 1-64 characters
   */
  public val input_field_placeholder: String? = null,
  /**
   * *Optional*. Use this parameter if you want to show the keyboard to specific users only. Targets: 1) users that are @mentioned in the *text* of the [Message](https://core.telegram.org/bots/api/#message) object; 2) if the bot's message is a reply (has *reply_to_message_id*), sender of the original message.  
   *
   * *Example:* A user requests to change the bot's language, bot replies to the request with a keyboard to select the new language. Other users in the group don't see the keyboard.
   */
  public val selective: Boolean? = null,
)
