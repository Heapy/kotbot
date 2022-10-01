package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.Serializable

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
   * Pass *True* if the administrator can access the chat event log, chat statistics, message statistics in channels, see channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any other administrator privilege
   */
  public val can_manage_chat: Boolean? = null,
  /**
   * Pass *True* if the administrator can create channel posts, channels only
   */
  public val can_post_messages: Boolean? = null,
  /**
   * Pass *True* if the administrator can edit messages of other users and can pin messages, channels only
   */
  public val can_edit_messages: Boolean? = null,
  /**
   * Pass *True* if the administrator can delete messages of other users
   */
  public val can_delete_messages: Boolean? = null,
  /**
   * Pass *True* if the administrator can manage video chats
   */
  public val can_manage_video_chats: Boolean? = null,
  /**
   * Pass *True* if the administrator can restrict, ban or unban chat members
   */
  public val can_restrict_members: Boolean? = null,
  /**
   * Pass *True* if the administrator can add new administrators with a subset of their own privileges or demote administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by him)
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
   * Pass *True* if the administrator can pin messages, supergroups only
   */
  public val can_pin_messages: Boolean? = null,
)
