package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatInviteLink
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to edit a subscription invite link created by the bot. The bot must have the *can_invite_users* administrator rights. Returns the edited invite link as a [ChatInviteLink](https://core.telegram.org/bots/api/#chatinvitelink) object.
 */
@Serializable
public data class EditChatSubscriptionInviteLink(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * The invite link to edit
     */
    public val invite_link: String,
    /**
     * Invite link name; 0-32 characters
     */
    public val name: String? = null,
) : Method<EditChatSubscriptionInviteLink, ChatInviteLink> by Companion {
    public companion object : Method<EditChatSubscriptionInviteLink, ChatInviteLink> {
        override val _deserializer: KSerializer<Response<ChatInviteLink>> =
                Response.serializer(ChatInviteLink.serializer())

        override val _serializer: KSerializer<EditChatSubscriptionInviteLink> = serializer()

        override val _name: String = "editChatSubscriptionInviteLink"
    }
}
