package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents one button of the reply keyboard. At most one of the optional fields must be used to specify type of the button. For simple text buttons, *String* can be used instead of this object to specify the button text.
 */
@Serializable
public data class KeyboardButton(
    /**
     * Text of the button. If none of the optional fields are used, it will be sent as a message when the button is pressed
     */
    public val text: String,
    /**
     * *Optional.* If specified, pressing the button will open a list of suitable users. Identifiers of selected users will be sent to the bot in a "users_shared" service message. Available in private chats only.
     */
    public val request_users: KeyboardButtonRequestUsers? = null,
    /**
     * *Optional.* If specified, pressing the button will open a list of suitable chats. Tapping on a chat will send its identifier to the bot in a "chat_shared" service message. Available in private chats only.
     */
    public val request_chat: KeyboardButtonRequestChat? = null,
    /**
     * *Optional*. If *True*, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only.
     */
    public val request_contact: Boolean? = null,
    /**
     * *Optional*. If *True*, the user's current location will be sent when the button is pressed. Available in private chats only.
     */
    public val request_location: Boolean? = null,
    /**
     * *Optional*. If specified, the user will be asked to create a poll and send it to the bot when the button is pressed. Available in private chats only.
     */
    public val request_poll: KeyboardButtonPollType? = null,
    /**
     * *Optional*. If specified, the described [Web App](https://core.telegram.org/bots/webapps) will be launched when the button is pressed. The Web App will be able to send a "web_app_data" service message. Available in private chats only.
     */
    public val web_app: WebAppInfo? = null,
)
