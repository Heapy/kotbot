package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the *can_manage_topics* administrator rights. Returns *True* on success.
 */
@Serializable
public data class CloseGeneralForumTopic(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
) : Method<CloseGeneralForumTopic, Boolean> by Companion {
    public companion object : Method<CloseGeneralForumTopic, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<CloseGeneralForumTopic> = serializer()

        override val _name: String = "closeGeneralForumTopic"
    }
}
