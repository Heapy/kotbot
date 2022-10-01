package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.BotCommand
import io.heapy.kotbot.bot.model.BotCommandScope
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Use this method to change the list of the bot's commands. See [https://core.telegram.org/bots#commands](https://core.telegram.org/bots#commands) for more details about bot commands. Returns *True* on success.
 */
@Serializable
public data class SetMyCommands(
  /**
   * A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
   */
  public val commands: List<BotCommand>,
  /**
   * A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
   */
  public val scope: BotCommandScope? = null,
  /**
   * A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
   */
  public val language_code: String? = null,
)
