package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputRichMessage
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to stream a partial rich message to a user while the message is being generated. Note that the streamed draft is ephemeral and acts as a temporary 30-second preview - once the output is finalized, you **must** call [sendRichMessage](https://core.telegram.org/bots/api/#sendrichmessage) with the complete message to persist it in the user's chat. Returns *True* on success.
 */
@Serializable
public data class SendRichMessageDraft(
    /**
     * Unique identifier for the target private chat
     */
    public val chat_id: Long,
    /**
     * Unique identifier for the target message thread
     */
    public val message_thread_id: Int? = null,
    /**
     * Unique identifier of the message draft; must be non-zero. Changes to drafts with the same identifier are animated.
     */
    public val draft_id: Int,
    /**
     * The partial message to be streamed
     */
    public val rich_message: InputRichMessage,
) : Method<SendRichMessageDraft, Boolean> by Companion {
    public companion object : Method<SendRichMessageDraft, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SendRichMessageDraft> = serializer()

        override val _name: String = "sendRichMessageDraft"
    }
}
