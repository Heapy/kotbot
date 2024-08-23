package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [chat member](https://core.telegram.org/bots/api/#chatmember) that isn't currently a member of the chat, but may join it themselves.
 */
@Serializable
public data class ChatMemberLeft(
    /**
     * The member's status in the chat, always "left"
     */
    public val status: String,
    /**
     * Information about the user
     */
    public val user: User,
) : ChatMember
