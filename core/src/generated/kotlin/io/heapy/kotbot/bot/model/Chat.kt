package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a chat.
 */
@Serializable
public data class Chat(
    /**
     * Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val id: Long,
    /**
     * Type of the chat, can be either "private", "group", "supergroup" or "channel"
     */
    public val type: String,
    /**
     * *Optional*. Title, for supergroups, channels and group chats
     */
    public val title: String? = null,
    /**
     * *Optional*. Username, for private chats, supergroups and channels if available
     */
    public val username: String? = null,
    /**
     * *Optional*. First name of the other party in a private chat
     */
    public val first_name: String? = null,
    /**
     * *Optional*. Last name of the other party in a private chat
     */
    public val last_name: String? = null,
    /**
     * *Optional*. *True*, if the supergroup chat is a forum (has [topics](https://telegram.org/blog/topics-in-groups-collectible-usernames#topics-in-groups) enabled)
     */
    public val is_forum: Boolean? = true,
)
