package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
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
   * Type of chat, can be either “private”, “group”, “supergroup” or “channel”
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
   * *Optional*. Chat photo. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val photo: ChatPhoto? = null,
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
   * *Optional*. For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user; in seconds. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val slow_mode_delay: Int? = null,
  /**
   * *Optional*. The time after which all messages sent to the chat will be automatically deleted; in seconds. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val message_auto_delete_time: Int? = null,
  /**
   * *Optional*. *True*, if messages from the chat can't be forwarded to other chats. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val has_protected_content: Boolean? = null,
  /**
   * *Optional*. For supergroups, name of group sticker set. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val sticker_set_name: String? = null,
  /**
   * *Optional*. *True*, if the bot can change the group sticker set. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val can_set_sticker_set: Boolean? = null,
  /**
   * *Optional*. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val linked_chat_id: Int? = null,
  /**
   * *Optional*. For supergroups, the location to which the supergroup is connected. Returned only in [getChat](https://core.telegram.org/bots/api/#getchat).
   */
  public val location: ChatLocation? = null,
)
