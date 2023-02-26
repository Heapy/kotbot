package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InputMedia
import io.heapy.kotbot.bot.model.Message
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of [Messages](https://core.telegram.org/bots/api/#message) that were sent is returned.
 */
@Serializable
public data class SendMediaGroup(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * A JSON-serialized array describing messages to be sent, must include 2-10 items
     */
    public val media: List<InputMedia>,
    /**
     * Sends messages [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent messages from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * If the messages are a reply, ID of the original message
     */
    public val reply_to_message_id: Int? = null,
    /**
     * Pass *True* if the message should be sent even if the specified replied-to message is not found
     */
    public val allow_sending_without_reply: Boolean? = null,
) : Method<SendMediaGroup, List<Message>> by Companion {
    public companion object : Method<SendMediaGroup, List<Message>> {
        override val _deserializer: KSerializer<Response<List<Message>>> =
                Response.serializer(ListSerializer(Message.serializer()))

        override val _serializer: KSerializer<SendMediaGroup> = serializer()

        override val _name: String = "sendMediaGroup"
    }
}
