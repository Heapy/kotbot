package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MessageEntity
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to stream a partial message to a user while the message is being generated. Note that the streamed draft is ephemeral and acts as a temporary 30-second preview - once the output is finalized, you **must** call [sendMessage](https://core.telegram.org/bots/api/#sendmessage) with the complete message to persist it in the user's chat. Returns *True* on success.
 */
@Serializable
public data class SendMessageDraft(
    /**
     * Unique identifier for the target private chat
     */
    public val chat_id: Long,
    /**
     * Unique identifier for the target message thread
     */
    public val message_thread_id: Int? = null,
    /**
     * Unique identifier of the message draft; must be non-zero. Changes of drafts with the same identifier are animated.
     */
    public val draft_id: Int,
    /**
     * Text of the message to be sent, 0-4096 characters after entities parsing. Pass an empty text to show a "Thinking…" placeholder.
     */
    public val text: String? = null,
    /**
     * Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
     */
    public val entities: List<MessageEntity>? = null,
) : Method<SendMessageDraft, Boolean> by Companion {
    public companion object : Method<SendMessageDraft, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SendMessageDraft> = serializer()

        override val _name: String = "sendMessageDraft"
    }
}
