package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and must have the *can_invite_users* administrator right. Returns *True* on success.
 */
@Serializable
public data class DeclineChatJoinRequest(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Unique identifier of the target user
   */
  public val user_id: Long,
)
