package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlinx.serialization.Serializable

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as *String* on success.
 */
@Serializable
public data class ExportChatInviteLink(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
)
