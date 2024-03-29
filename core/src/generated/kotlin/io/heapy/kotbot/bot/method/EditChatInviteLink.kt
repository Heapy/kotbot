package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatInviteLink
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a [ChatInviteLink](https://core.telegram.org/bots/api/#chatinvitelink) object.
 */
@Serializable
public data class EditChatInviteLink(
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
    /**
     * Point in time (Unix timestamp) when the link will expire
     */
    public val expire_date: Long? = null,
    /**
     * The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
     */
    public val member_limit: Int? = null,
    /**
     * *True*, if users joining the chat via the link need to be approved by chat administrators. If *True*, *member_limit* can't be specified
     */
    public val creates_join_request: Boolean? = null,
) : Method<EditChatInviteLink, ChatInviteLink> by Companion {
    public companion object : Method<EditChatInviteLink, ChatInviteLink> {
        override val _deserializer: KSerializer<Response<ChatInviteLink>> =
                Response.serializer(ChatInviteLink.serializer())

        override val _serializer: KSerializer<EditChatInviteLink> = serializer()

        override val _name: String = "editChatInviteLink"
    }
}
