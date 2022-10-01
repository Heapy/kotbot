package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.BotCommandScope
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of [BotCommand](https://core.telegram.org/bots/api/#botcommand) objects. If commands aren't set, an empty list is returned.
 */
@Serializable
public data class GetMyCommands(
  /**
   * A JSON-serialized object, describing scope of users. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
   */
  public val scope: BotCommandScope? = null,
  /**
   * A two-letter ISO 639-1 language code or an empty string
   */
  public val language_code: String? = null,
)
