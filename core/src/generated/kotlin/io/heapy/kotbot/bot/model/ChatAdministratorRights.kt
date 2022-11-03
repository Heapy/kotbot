package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Represents the rights of an administrator in a chat.
 */
@Serializable
public data class ChatAdministratorRights(
  /**
   * *True*, if the user's presence in the chat is hidden
   */
  public val is_anonymous: Boolean,
  /**
   * *True*, if the administrator can access the chat event log, chat statistics, message statistics in channels, see channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any other administrator privilege
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
   * *True*, if the administrator can restrict, ban or unban chat members
   */
  public val can_restrict_members: Boolean,
  /**
   * *True*, if the administrator can add new administrators with a subset of their own privileges or demote administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by the user)
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
   * *Optional*. *True*, if the administrator can post in the channel; channels only
   */
  public val can_post_messages: Boolean? = null,
  /**
   * *Optional*. *True*, if the administrator can edit messages of other users and can pin messages; channels only
   */
  public val can_edit_messages: Boolean? = null,
  /**
   * *Optional*. *True*, if the user is allowed to pin messages; groups and supergroups only
   */
  public val can_pin_messages: Boolean? = null,
)