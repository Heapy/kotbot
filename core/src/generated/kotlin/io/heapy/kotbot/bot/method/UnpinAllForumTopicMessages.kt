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
 * Use this method to clear the list of pinned messages in a forum topic. The bot must be an administrator in the chat for this to work and must have the *can_pin_messages* administrator right in the supergroup. Returns *True* on success.
 */
@Serializable
public data class UnpinAllForumTopicMessages(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread of the forum topic
     */
    public val message_thread_id: Int,
) : Method<UnpinAllForumTopicMessages, Boolean> by Companion {
    public companion object : Method<UnpinAllForumTopicMessages, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<UnpinAllForumTopicMessages> = serializer()

        override val _name: String = "unpinAllForumTopicMessages"
    }
}
