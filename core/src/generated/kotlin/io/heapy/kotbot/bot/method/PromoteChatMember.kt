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
 * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Pass *False* for all boolean parameters to demote a user. Returns *True* on success.
 */
@Serializable
public data class PromoteChatMember(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * Pass *True* if the administrator's presence in the chat is hidden
     */
    public val is_anonymous: Boolean? = null,
    /**
     * Pass *True* if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
     */
    public val can_manage_chat: Boolean? = null,
    /**
     * Pass *True* if the administrator can delete messages of other users
     */
    public val can_delete_messages: Boolean? = null,
    /**
     * Pass *True* if the administrator can manage video chats
     */
    public val can_manage_video_chats: Boolean? = null,
    /**
     * Pass *True* if the administrator can restrict, ban or unban chat members, or access supergroup statistics
     */
    public val can_restrict_members: Boolean? = null,
    /**
     * Pass *True* if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by him)
     */
    public val can_promote_members: Boolean? = null,
    /**
     * Pass *True* if the administrator can change chat title, photo and other settings
     */
    public val can_change_info: Boolean? = null,
    /**
     * Pass *True* if the administrator can invite new users to the chat
     */
    public val can_invite_users: Boolean? = null,
    /**
     * Pass *True* if the administrator can post stories to the chat
     */
    public val can_post_stories: Boolean? = null,
    /**
     * Pass *True* if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
     */
    public val can_edit_stories: Boolean? = null,
    /**
     * Pass *True* if the administrator can delete stories posted by other users
     */
    public val can_delete_stories: Boolean? = null,
    /**
     * Pass *True* if the administrator can post messages in the channel, or access channel statistics; for channels only
     */
    public val can_post_messages: Boolean? = null,
    /**
     * Pass *True* if the administrator can edit messages of other users and can pin messages; for channels only
     */
    public val can_edit_messages: Boolean? = null,
    /**
     * Pass *True* if the administrator can pin messages; for supergroups only
     */
    public val can_pin_messages: Boolean? = null,
    /**
     * Pass *True* if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
     */
    public val can_manage_topics: Boolean? = null,
) : Method<PromoteChatMember, Boolean> by Companion {
    public companion object : Method<PromoteChatMember, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<PromoteChatMember> = serializer()

        override val _name: String = "promoteChatMember"
    }
}
