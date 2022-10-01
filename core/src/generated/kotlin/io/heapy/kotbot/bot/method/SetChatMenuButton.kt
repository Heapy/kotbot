package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.MenuButton
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns *True* on success.
 */
@Serializable
public data class SetChatMenuButton(
  /**
   * Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
   */
  public val chat_id: Long? = null,
  /**
   * A JSON-serialized object for the bot's new menu button. Defaults to [MenuButtonDefault](https://core.telegram.org/bots/api/#menubuttondefault)
   */
  public val menu_button: MenuButton? = null,
)
