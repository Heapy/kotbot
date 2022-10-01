package io.heapy.kotbot.bot.method

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns [MenuButton](https://core.telegram.org/bots/api/#menubutton) on success.
 */
@Serializable
public data class GetChatMenuButton(
  /**
   * Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
   */
  public val chat_id: Long? = null,
)
