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
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have *can_manage_topics* administrator rights. Returns *True* on success.
 */
@Serializable
public data class EditGeneralForumTopic(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
    /**
     * New topic name, 1-128 characters
     */
    public val name: String,
) : Method<EditGeneralForumTopic, Boolean> by Companion {
    public companion object : Method<EditGeneralForumTopic, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<EditGeneralForumTopic> = serializer()

        override val _name: String = "editGeneralForumTopic"
    }
}
