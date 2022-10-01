package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlinx.serialization.Serializable

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns *True* on success.
 */
@Serializable
public data class LeaveChat(
  /**
   * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
)
