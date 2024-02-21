package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
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
     * Type of chat, can be either "private", "group", "supergroup" or "channel"
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
    public val is_forum: Boolean? = null,
    /**
     * *Optional*. Chat photo. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val photo: ChatPhoto? = null,
    /**
     * *Optional*. If non-empty, the list of all [active chat usernames](https://telegram.org/blog/topics-in-groups-collectible-usernames#collectible-usernames); for private chats, supergroups and channels. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val active_usernames: List<String>? = null,
    /**
     * *Optional*. List of available reactions allowed in the chat. If omitted, then all [emoji reactions](https://core.telegram.org/bots/api/#reactiontypeemoji) are allowed. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val available_reactions: List<ReactionType>? = null,
    /**
     * *Optional*. Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header, and link preview. See [accent colors](https://core.telegram.org/bots/api/#accent-colors) for more details. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat). Always returned in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val accent_color_id: Int? = null,
    /**
     * *Optional*. Custom emoji identifier of emoji chosen by the chat for the reply header and link preview background. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val background_custom_emoji_id: String? = null,
    /**
     * *Optional*. Identifier of the accent color for the chat's profile background. See [profile accent colors](https://core.telegram.org/bots/api/#profile-accent-colors) for more details. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val profile_accent_color_id: Int? = null,
    /**
     * *Optional*. Custom emoji identifier of the emoji chosen by the chat for its profile background. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val profile_background_custom_emoji_id: String? = null,
    /**
     * *Optional*. Custom emoji identifier of the emoji status of the chat or the other party in a private chat. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val emoji_status_custom_emoji_id: String? = null,
    /**
     * *Optional*. Expiration date of the emoji status of the chat or the other party in a private chat, in Unix time, if any. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val emoji_status_expiration_date: Long? = null,
    /**
     * *Optional*. Bio of the other party in a private chat. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val bio: String? = null,
    /**
     * *Optional*. *True*, if privacy settings of the other party in the private chat allows to use `tg://user?id=<user_id>` links only in chats with the user. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val has_private_forwards: Boolean? = null,
    /**
     * *Optional*. *True*, if the privacy settings of the other party restrict sending voice and video note messages in the private chat. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val has_restricted_voice_and_video_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if users need to join the supergroup before they can send messages. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val join_to_send_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if all users directly joining the supergroup need to be approved by supergroup administrators. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val join_by_request: Boolean? = null,
    /**
     * *Optional*. Description, for groups, supergroups and channel chats. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val description: String? = null,
    /**
     * *Optional*. Primary invite link, for groups, supergroups and channel chats. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val invite_link: String? = null,
    /**
     * *Optional*. The most recent pinned message (by sending date). Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val pinned_message: Message? = null,
    /**
     * *Optional*. Default chat member permissions, for groups and supergroups. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val permissions: ChatPermissions? = null,
    /**
     * *Optional*. For supergroups, the minimum allowed delay between consecutive messages sent by each unprivileged user; in seconds. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val slow_mode_delay: Int? = null,
    /**
     * *Optional*. For supergroups, the minimum number of boosts that a non-administrator user needs to add in order to ignore slow mode and chat permissions. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val unrestrict_boost_count: Int? = null,
    /**
     * *Optional*. The time after which all messages sent to the chat will be automatically deleted; in seconds. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val message_auto_delete_time: Int? = null,
    /**
     * *Optional*. *True*, if aggressive anti-spam checks are enabled in the supergroup. The field is only available to chat administrators. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val has_aggressive_anti_spam_enabled: Boolean? = null,
    /**
     * *Optional*. *True*, if non-administrators can only get the list of bots and administrators in the chat. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val has_hidden_members: Boolean? = null,
    /**
     * *Optional*. *True*, if messages from the chat can't be forwarded to other chats. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val has_protected_content: Boolean? = null,
    /**
     * *Optional*. *True*, if new chat members will have access to old messages; available only to chat administrators. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val has_visible_history: Boolean? = null,
    /**
     * *Optional*. For supergroups, name of group sticker set. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val sticker_set_name: String? = null,
    /**
     * *Optional*. *True*, if the bot can change the group sticker set. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val can_set_sticker_set: Boolean? = null,
    /**
     * *Optional*. For supergroups, the name of the group's custom emoji sticker set. Custom emoji from this set can be used by all users and bots in the group. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val custom_emoji_sticker_set_name: String? = null,
    /**
     * *Optional*. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val linked_chat_id: Long? = null,
    /**
     * *Optional*. For supergroups, the location to which the supergroup is connected. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
     */
    public val location: ChatLocation? = null,
)
