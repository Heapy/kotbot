package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlinx.serialization.Serializable

/**
 * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns *True* on success.
 */
@Serializable
public data class DeleteChatPhoto(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
)
