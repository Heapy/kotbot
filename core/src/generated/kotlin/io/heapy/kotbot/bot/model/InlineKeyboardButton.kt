package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents one button of an inline keyboard. You **must** use exactly one of the optional fields.
 */
@Serializable
public data class InlineKeyboardButton(
  /**
   * Label text on the button
   */
  public val text: String,
  /**
   * *Optional*. HTTP or tg:// URL to be opened when the button is pressed. Links `tg://user?id=<user_id>` can be used to mention a user by their ID without using a username, if this is allowed by their privacy settings.
   */
  public val url: String? = null,
  /**
   * *Optional*. Data to be sent in a [callback query](https://core.telegram.org/bots/api/#callbackquery) to the bot when button is pressed, 1-64 bytes
   */
  public val callback_data: String? = null,
  /**
   * *Optional*. Description of the [Web App](https://core.telegram.org/bots/webapps) that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method [answerWebAppQuery](https://core.telegram.org/bots/api/#answerwebappquery). Available only in private chats between a user and the bot.
   */
  public val web_app: WebAppInfo? = null,
  /**
   * *Optional*. An HTTPS URL used to automatically authorize the user. Can be used as a replacement for the [Telegram Login Widget](https://core.telegram.org/widgets/login).
   */
  public val login_url: LoginUrl? = null,
  /**
   * *Optional*. If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the bot's username and the specified inline query in the input field. May be empty, in which case just the bot's username will be inserted.  
   *
   * **Note:** This offers an easy way for users to start using your bot in [inline mode](https://core.telegram.org/bots/inline) when they are currently in a private chat with it. Especially useful when combined with [*switch\_pm…*](https://core.telegram.org/bots/api/#answerinlinequery) actions - in this case the user will be automatically returned to the chat they switched from, skipping the chat selection screen.
   */
  public val switch_inline_query: String? = null,
  /**
   * *Optional*. If set, pressing the button will insert the bot's username and the specified inline query in the current chat's input field. May be empty, in which case only the bot's username will be inserted.  
   *
   * This offers a quick way for the user to open your bot in inline mode in the same chat - good for selecting something from multiple options.
   */
  public val switch_inline_query_current_chat: String? = null,
  /**
   * *Optional*. Description of the game that will be launched when the user presses the button.  
   *
   * **NOTE:** This type of button **must** always be the first button in the first row.
   */
  public val callback_game: CallbackGame? = null,
  /**
   * *Optional*. Specify *True*, to send a [Pay button](https://core.telegram.org/bots/api/#payments).  
   *
   * **NOTE:** This type of button **must** always be the first button in the first row and can only be used in invoice messages.
   */
  public val pay: Boolean? = null,
)
