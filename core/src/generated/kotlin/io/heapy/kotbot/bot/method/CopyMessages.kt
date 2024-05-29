package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.MessageId
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they are skipped. Service messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz [poll](https://core.telegram.org/bots/api/#poll) can be copied only if the value of the field *correct_option_id* is known to the bot. The method is analogous to the method [forwardMessages](https://core.telegram.org/bots/api/#forwardmessages), but the copied messages don't have a link to the original message. Album grouping is kept for copied messages. On success, an array of [MessageId](https://core.telegram.org/bots/api/#messageid) of the sent messages is returned.
 */
@Serializable
public data class CopyMessages(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * Unique identifier for the chat where the original messages were sent (or channel username in the format `@channelusername`)
     */
    public val from_chat_id: ChatId,
    /**
     * A JSON-serialized list of 1-100 identifiers of messages in the chat *from_chat_id* to copy. The identifiers must be specified in a strictly increasing order.
     */
    public val message_ids: List<Int>,
    /**
     * Sends the messages [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent messages from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * Pass *True* to copy the messages without their captions
     */
    public val remove_caption: Boolean? = null,
) : Method<CopyMessages, List<MessageId>> by Companion {
    public companion object : Method<CopyMessages, List<MessageId>> {
        override val _deserializer: KSerializer<Response<List<MessageId>>> =
                Response.serializer(ListSerializer(MessageId.serializer()))

        override val _serializer: KSerializer<CopyMessages> = serializer()

        override val _name: String = "copyMessages"
    }
}
