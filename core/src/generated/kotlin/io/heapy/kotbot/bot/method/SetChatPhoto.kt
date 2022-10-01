package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InputFile
import kotlinx.serialization.Serializable

/**
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns *True* on success.
 */
@Serializable
public data class SetChatPhoto(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * New chat photo, uploaded using multipart/form-data
   */
  public val photo: InputFile,
)
