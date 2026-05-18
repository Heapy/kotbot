package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to remove up to 10000 recent reactions in a group or a supergroup chat added by a given user or chat. The bot must have the 'can_delete_messages' administrator right in the chat. Returns *True* on success.
 */
@Serializable
public data class DeleteAllMessageReactions(
    /**
     * Unique identifier for the target chat or username of the target supergroup in the format `@username`
     */
    public val chat_id: ChatId,
    /**
     * Identifier of the user whose reactions will be removed, if the reactions were added by a user
     */
    public val user_id: Long? = null,
    /**
     * Identifier of the chat whose reactions will be removed, if the reactions were added by a chat
     */
    public val actor_chat_id: Long? = null,
) : Method<DeleteAllMessageReactions, Boolean> by Companion {
    public companion object : Method<DeleteAllMessageReactions, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteAllMessageReactions> = serializer()

        override val _name: String = "deleteAllMessageReactions"
    }
}
