package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [chat member](https://core.telegram.org/bots/api/#chatmember) that was banned in the chat and can't return to the chat or view chat messages.
 */
@Serializable
public data class ChatMemberBanned(
  /**
   * The member's status in the chat, always “kicked”
   */
  public val status: String,
  /**
   * Information about the user
   */
  public val user: User,
  /**
   * Date when restrictions will be lifted for this user; unix time. If 0, then the user is banned forever
   */
  public val until_date: Int,
) : ChatMember
