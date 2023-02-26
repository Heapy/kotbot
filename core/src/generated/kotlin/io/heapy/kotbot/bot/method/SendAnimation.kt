package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Animation
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.ReplyMarkup
import io.heapy.kotbot.bot.model.Thumbnail
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
 */
@Serializable
public data class SendAnimation(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val animation: Animation,
    /**
     * Duration of sent animation in seconds
     */
    public val duration: Int? = null,
    /**
     * Animation width
     */
    public val width: Int? = null,
    /**
     * Animation height
     */
    public val height: Int? = null,
    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val thumbnail: Thumbnail? = null,
    /**
     * Animation caption (may also be used when resending animation by *file_id*), 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the animation caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * Pass *True* if the animation needs to be covered with a spoiler animation
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
) : Method<SendAnimation, Message> by Companion {
    public companion object : Method<SendAnimation, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<SendAnimation> = serializer()

        override val _name: String = "sendAnimation"
    }
}
