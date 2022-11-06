package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [chat member](https://core.telegram.org/bots/api/#chatmember) that is under certain restrictions in the chat. Supergroups only.
 */
@Serializable
public data class ChatMemberRestricted(
    /**
     * The member's status in the chat, always “restricted”
     */
    public val status: String = "restricted",
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *True*, if the user is a member of the chat at the moment of the request
     */
    public val is_member: Boolean,
    /**
     * *True*, if the user is allowed to change the chat title, photo and other settings
     */
    public val can_change_info: Boolean,
    /**
     * *True*, if the user is allowed to invite new users to the chat
     */
    public val can_invite_users: Boolean,
    /**
     * *True*, if the user is allowed to pin messages
     */
    public val can_pin_messages: Boolean,
    /**
     * *True*, if the user is allowed to create forum topics
     */
    public val can_manage_topics: Boolean,
    /**
     * *True*, if the user is allowed to send text messages, contacts, locations and venues
     */
    public val can_send_messages: Boolean,
    /**
     * *True*, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes
     */
    public val can_send_media_messages: Boolean,
    /**
     * *True*, if the user is allowed to send polls
     */
    public val can_send_polls: Boolean,
    /**
     * *True*, if the user is allowed to send animations, games, stickers and use inline bots
     */
    public val can_send_other_messages: Boolean,
    /**
     * *True*, if the user is allowed to add web page previews to their messages
     */
    public val can_add_web_page_previews: Boolean,
    /**
     * Date when restrictions will be lifted for this user; unix time. If 0, then the user is restricted forever
     */
    public val until_date: Long,
) : ChatMember
