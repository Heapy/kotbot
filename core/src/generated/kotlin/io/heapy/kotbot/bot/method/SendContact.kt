package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ReplyMarkup
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to send phone contacts. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendContact(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * Contact's phone number
     */
    public val phone_number: String,
    /**
     * Contact's first name
     */
    public val first_name: String,
    /**
     * Contact's last name
     */
    public val last_name: String? = null,
    /**
     * Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
     */
    public val vcard: String? = null,
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
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     */
    public val reply_markup: ReplyMarkup? = null,
) : Method<SendContact, Message> by Companion {
    public companion object : Method<SendContact, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<SendContact> = serializer()

        override val _name: String = "sendContact"
    }
}
