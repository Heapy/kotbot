package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlinx.serialization.Serializable

/**
 * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of [ChatMember](https://core.telegram.org/bots/api/#chatmember) objects.
 */
@Serializable
public data class GetChatAdministrators(
  /**
   * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
)
