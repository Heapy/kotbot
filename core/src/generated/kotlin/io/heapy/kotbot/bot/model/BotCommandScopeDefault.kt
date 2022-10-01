package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the default [scope](https://core.telegram.org/bots/api/#botcommandscope) of bot commands. Default commands are used if no commands with a [narrower scope](https://core.telegram.org/bots/api/#determining-list-of-commands) are specified for the user.
 */
@Serializable
public data class BotCommandScopeDefault(
  /**
   * Scope type, must be *default*
   */
  public val type: String = "default",
) : BotCommandScope
