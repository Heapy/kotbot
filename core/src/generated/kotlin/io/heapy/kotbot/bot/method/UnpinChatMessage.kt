package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns *True* on success.
 */
@Serializable
public data class UnpinChatMessage(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Identifier of a message to unpin. If not specified, the most recent pinned message (by sending date) will be unpinned.
   */
  public val message_id: Int? = null,
)
