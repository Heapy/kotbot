package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [chat member](https://core.telegram.org/bots/api/#chatmember) that has some additional privileges.
 */
@Serializable
public data class ChatMemberAdministrator(
    /**
     * The member's status in the chat, always "administrator"
     */
    public val status: String = "administrator",
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *True*, if the bot is allowed to edit administrator privileges of that user
     */
    public val can_be_edited: Boolean,
    /**
     * *True*, if the user's presence in the chat is hidden
     */
    public val is_anonymous: Boolean,
    /**
     * *True*, if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
     */
    public val can_manage_chat: Boolean,
    /**
     * *True*, if the administrator can delete messages of other users
     */
    public val can_delete_messages: Boolean,
    /**
     * *True*, if the administrator can manage video chats
     */
    public val can_manage_video_chats: Boolean,
    /**
     * *True*, if the administrator can restrict, ban or unban chat members, or access supergroup statistics
     */
    public val can_restrict_members: Boolean,
    /**
     * *True*, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
     */
    public val can_promote_members: Boolean,
    /**
     * *True*, if the user is allowed to change the chat title, photo and other settings
     */
    public val can_change_info: Boolean,
    /**
     * *True*, if the user is allowed to invite new users to the chat
     */
    public val can_invite_users: Boolean,
    /**
     * *True*, if the administrator can post stories to the chat
     */
    public val can_post_stories: Boolean,
    /**
     * *True*, if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
     */
    public val can_edit_stories: Boolean,
    /**
     * *True*, if the administrator can delete stories posted by other users
     */
    public val can_delete_stories: Boolean,
    /**
     * *Optional*. *True*, if the administrator can post messages in the channel, or access channel statistics; for channels only
     */
    public val can_post_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the administrator can edit messages of other users and can pin messages; for channels only
     */
    public val can_edit_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to pin messages; for groups and supergroups only
     */
    public val can_pin_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
     */
    public val can_manage_topics: Boolean? = null,
    /**
     * *Optional*. Custom title for this user
     */
    public val custom_title: String? = null,
) : ChatMember
