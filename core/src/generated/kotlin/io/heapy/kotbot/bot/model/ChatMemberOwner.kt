package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [chat member](https://core.telegram.org/bots/api/#chatmember) that owns the chat and has all administrator privileges.
 */
@Serializable
public data class ChatMemberOwner(
    /**
     * The member's status in the chat, always “creator”
     */
    public val status: String = "creator",
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *True*, if the user's presence in the chat is hidden
     */
    public val is_anonymous: Boolean,
    /**
     * *Optional*. Custom title for this user
     */
    public val custom_title: String? = null,
) : ChatMember
