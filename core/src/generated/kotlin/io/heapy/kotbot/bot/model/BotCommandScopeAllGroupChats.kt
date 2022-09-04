package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the [scope](https://core.telegram.org/bots/api/#botcommandscope) of bot commands, covering all group and supergroup chats.
 */
@Serializable
public data class BotCommandScopeAllGroupChats(
  /**
   * Scope type, must be *all\_group\_chats*
   */
  public val type: String,
)
