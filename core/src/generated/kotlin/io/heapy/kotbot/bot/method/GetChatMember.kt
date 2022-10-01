package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to get information about a member of a chat. Returns a [ChatMember](https://core.telegram.org/bots/api/#chatmember) object on success.
 */
@Serializable
public data class GetChatMember(
  /**
   * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Unique identifier of the target user
   */
  public val user_id: Long,
)
