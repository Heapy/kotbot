package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InputFile
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.ReplyMarkup
import io.heapy.kotbot.bot.model.ReplyParameters
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to send photos. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendPhoto(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    public val business_connection_id: String? = null,
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val photo: InputFile,
    /**
     * Photo caption (may also be used when resending photos by *file_id*), 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the photo caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * Pass *True*, if the caption must be shown above the message media
     */
    public val show_caption_above_media: Boolean? = null,
    /**
     * Pass *True* if the photo needs to be covered with a spoiler animation
     */
    public val has_spoiler: Boolean? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; for private chats only
     */
    public val message_effect_id: String? = null,
    /**
     * Description of the message to reply to
     */
    public val reply_parameters: ReplyParameters? = null,
    /**
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
     */
    public val reply_markup: ReplyMarkup? = null,
) : Method<SendPhoto, Message> by Companion {
    public companion object : Method<SendPhoto, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<SendPhoto> = serializer()

        override val _name: String = "sendPhoto"
    }
}
