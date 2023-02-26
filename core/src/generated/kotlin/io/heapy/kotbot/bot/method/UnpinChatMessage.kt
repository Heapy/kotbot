package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns *True* on success.
 */
@Serializable
public data class UnpinChatMessage(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Identifier of a message to unpin. If not specified, the most recent pinned message (by sending date) will be unpinned.
     */
    public val message_id: Int? = null,
) : Method<UnpinChatMessage, Boolean> by Companion {
    public companion object : Method<UnpinChatMessage, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<UnpinChatMessage> = serializer()

        override val _name: String = "unpinChatMessage"
    }
}
