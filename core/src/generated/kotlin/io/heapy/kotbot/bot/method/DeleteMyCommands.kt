package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.BotCommandScope
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion, [higher level commands](https://core.telegram.org/bots/api/#determining-list-of-commands) will be shown to affected users. Returns *True* on success.
 */
@Serializable
public data class DeleteMyCommands(
  /**
   * A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
   */
  public val scope: BotCommandScope? = null,
  /**
   * A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
   */
  public val language_code: String? = null,
)
