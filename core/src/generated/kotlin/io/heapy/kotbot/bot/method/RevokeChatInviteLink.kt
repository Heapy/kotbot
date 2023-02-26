package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatInviteLink
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is automatically generated. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the revoked invite link as [ChatInviteLink](https://core.telegram.org/bots/api/#chatinvitelink) object.
 */
@Serializable
public data class RevokeChatInviteLink(
    /**
     * Unique identifier of the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * The invite link to revoke
     */
    public val invite_link: String,
) : Method<RevokeChatInviteLink, ChatInviteLink> by Companion {
    public companion object : Method<RevokeChatInviteLink, ChatInviteLink> {
        override val _deserializer: KSerializer<Response<ChatInviteLink>> =
                Response.serializer(ChatInviteLink.serializer())

        override val _serializer: KSerializer<RevokeChatInviteLink> = serializer()

        override val _name: String = "revokeChatInviteLink"
    }
}
