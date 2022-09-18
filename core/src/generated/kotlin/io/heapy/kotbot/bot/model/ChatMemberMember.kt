package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [chat member](https://core.telegram.org/bots/api/#chatmember) that has no additional privileges or restrictions.
 */
@Serializable
public data class ChatMemberMember(
  /**
   * The member's status in the chat, always “member”
   */
  public val status: String,
  /**
   * Information about the user
   */
  public val user: User,
) : ChatMember
