package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is [unbanned](https://core.telegram.org/bots/api/#unbanchatsenderchat), the owner of the banned chat won't be able to send messages on behalf of **any of their channels**. The bot must be an administrator in the supergroup or channel for this to work and must have the appropriate administrator rights. Returns *True* on success.
 */
@Serializable
public data class BanChatSenderChat(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Unique identifier of the target sender chat
   */
  public val sender_chat_id: Long,
)
