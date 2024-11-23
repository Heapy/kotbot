package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents one button of an inline keyboard. Exactly one of the optional fields must be used to specify type of the button.
 */
@Serializable
public data class InlineKeyboardButton(
    /**
     * Label text on the button
     */
    public val text: String,
    /**
     * *Optional*. HTTP or tg:// URL to be opened when the button is pressed. Links `tg://user?id=<user_id>` can be used to mention a user by their identifier without using a username, if this is allowed by their privacy settings.
     */
    public val url: String? = null,
    /**
     * *Optional*. Data to be sent in a [callback query](https://core.telegram.org/bots/api/#callbackquery) to the bot when the button is pressed, 1-64 bytes
     */
    public val callback_data: String? = null,
    /**
     * *Optional*. Description of the [Web App](https://core.telegram.org/bots/webapps) that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method [answerWebAppQuery](https://core.telegram.org/bots/api/#answerwebappquery). Available only in private chats between a user and the bot. Not supported for messages sent on behalf of a Telegram Business account.
     */
    public val web_app: WebAppInfo? = null,
    /**
     * *Optional*. An HTTPS URL used to automatically authorize the user. Can be used as a replacement for the [Telegram Login Widget](https://core.telegram.org/widgets/login).
     */
    public val login_url: LoginUrl? = null,
    /**
     * *Optional*. If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the bot's username and the specified inline query in the input field. May be empty, in which case just the bot's username will be inserted. Not supported for messages sent on behalf of a Telegram Business account.
     */
    public val switch_inline_query: String? = null,
    /**
     * *Optional*. If set, pressing the button will insert the bot's username and the specified inline query in the current chat's input field. May be empty, in which case only the bot's username will be inserted.  
     *
     * This offers a quick way for the user to open your bot in inline mode in the same chat - good for selecting something from multiple options. Not supported in channels and for messages sent on behalf of a Telegram Business account.
     */
    public val switch_inline_query_current_chat: String? = null,
    /**
     * *Optional*. If set, pressing the button will prompt the user to select one of their chats of the specified type, open that chat and insert the bot's username and the specified inline query in the input field. Not supported for messages sent on behalf of a Telegram Business account.
     */
    public val switch_inline_query_chosen_chat: SwitchInlineQueryChosenChat? = null,
    /**
     * *Optional*. Description of the button that copies the specified text to the clipboard.
     */
    public val copy_text: CopyTextButton? = null,
    /**
     * *Optional*. Description of the game that will be launched when the user presses the button.  
     *
     * **NOTE:** This type of button **must** always be the first button in the first row.
     */
    public val callback_game: CallbackGame? = null,
    /**
     * *Optional*. Specify *True*, to send a [Pay button](https://core.telegram.org/bots/api/#payments). Substrings "⭐" and "XTR" in the buttons's text will be replaced with a Telegram Star icon.  
     *
     * **NOTE:** This type of button **must** always be the first button in the first row and can only be used in invoice messages.
     */
    public val pay: Boolean? = null,
)
