package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MenuButton
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Long
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns [MenuButton](https://core.telegram.org/bots/api/#menubutton) on success.
 */
@Serializable
public data class GetChatMenuButton(
  /**
   * Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
   */
  public val chat_id: Long? = null,
) : Method<MenuButton> {
  public override suspend fun Kotbot.execute(): MenuButton = requestForJson(
    name = "getChatMenuButton",
    serialize = {
      json.encodeToString(
        serializer(),
        this@GetChatMenuButton
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<MenuButton>> =
        Response.serializer(MenuButton.serializer())
  }
}
