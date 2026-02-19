package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.SuggestedPostParameters
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to forward messages of any kind. Service messages and messages with protected content can't be forwarded. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class ForwardMessage(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
     */
    public val message_thread_id: Int? = null,
    /**
     * Identifier of the direct messages topic to which the message will be forwarded; required if the message is forwarded to a direct messages chat
     */
    public val direct_messages_topic_id: Int? = null,
    /**
     * Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     */
    public val from_chat_id: ChatId,
    /**
     * New start timestamp for the forwarded video in the message
     */
    public val video_start_timestamp: Int? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the forwarded message from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; only available when forwarding to private chats
     */
    public val message_effect_id: String? = null,
    /**
     * A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only
     */
    public val suggested_post_parameters: SuggestedPostParameters? = null,
    /**
     * Message identifier in the chat specified in *from_chat_id*
     */
    public val message_id: Int,
) : Method<ForwardMessage, Message> by Companion {
    public companion object : Method<ForwardMessage, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<ForwardMessage> = serializer()

        override val _name: String = "forwardMessage"
    }
}
