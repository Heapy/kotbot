package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotCommand
import io.heapy.kotbot.bot.model.BotCommandScope
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

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
) : Method<List<BotCommand>> {
  public override suspend fun Kotbot.execute(): List<BotCommand> = requestForJson(
    name = "getMyCommands",
    serialize = {
      json.encodeToString(
        serializer(),
        this@GetMyCommands
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<List<BotCommand>>> =
        Response.serializer(ListSerializer(BotCommand.serializer()))
  }
}
