package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a menu button, which opens the bot's list of commands.
 */
@Serializable
public data class MenuButtonCommands(
  /**
   * Type of the button, must be *commands*
   */
  public val type: String,
) : MenuButton
