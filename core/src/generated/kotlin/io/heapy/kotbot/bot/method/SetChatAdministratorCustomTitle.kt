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
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns *True* on success.
 */
@Serializable
public data class SetChatAdministratorCustomTitle(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * New custom title for the administrator; 0-16 characters, emoji are not allowed
     */
    public val custom_title: String,
) : Method<SetChatAdministratorCustomTitle, Boolean> by Companion {
    public companion object : Method<SetChatAdministratorCustomTitle, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetChatAdministratorCustomTitle> = serializer()

        override val _name: String = "setChatAdministratorCustomTitle"
    }
}
