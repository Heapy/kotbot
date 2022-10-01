package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object represents changes in the status of a chat member.
 */
@Serializable
public data class ChatMemberUpdated(
  /**
   * Chat the user belongs to
   */
  public val chat: Chat,
  /**
   * Performer of the action, which resulted in the change
   */
  public val from: User,
  /**
   * Date the change was done in Unix time
   */
  public val date: Long,
  /**
   * Previous information about the chat member
   */
  public val old_chat_member: ChatMember,
  /**
   * New information about the chat member
   */
  public val new_chat_member: ChatMember,
  /**
   * *Optional*. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
   */
  public val invite_link: ChatInviteLink? = null,
)
