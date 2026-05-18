package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to remove a reaction from a message in a group or a supergroup chat. The bot must have the 'can_delete_messages' administrator right in the chat. Returns *True* on success.
 */
@Serializable
public data class DeleteMessageReaction(
    /**
     * Unique identifier for the target chat or username of the target supergroup in the format `@username`
     */
    public val chat_id: ChatId,
    /**
     * Identifier of the target message
     */
    public val message_id: Int,
    /**
     * Identifier of the user whose reaction will be removed, if the reaction was added by a user
     */
    public val user_id: Long? = null,
    /**
     * Identifier of the chat whose reaction will be removed, if the reaction was added by a chat
     */
    public val actor_chat_id: Long? = null,
) : Method<DeleteMessageReaction, Boolean> by Companion {
    public companion object : Method<DeleteMessageReaction, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteMessageReaction> = serializer()

        override val _name: String = "deleteMessageReaction"
    }
}
