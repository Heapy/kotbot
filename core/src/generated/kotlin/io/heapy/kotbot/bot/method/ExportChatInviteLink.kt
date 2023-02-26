package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as *String* on success.
 */
@Serializable
public data class ExportChatInviteLink(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
) : Method<ExportChatInviteLink, String> by Companion {
    public companion object : Method<ExportChatInviteLink, String> {
        override val _deserializer: KSerializer<Response<String>> =
                Response.serializer(String.serializer())

        override val _serializer: KSerializer<ExportChatInviteLink> = serializer()

        override val _name: String = "exportChatInviteLink"
    }
}
