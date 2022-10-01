package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlinx.serialization.Serializable

/**
 * Use this method to get the number of members in a chat. Returns *Int* on success.
 */
@Serializable
public data class GetChatMemberCount(
  /**
   * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
)
