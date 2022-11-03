package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns *True* on success.
 */
@Serializable
public data class UnpinAllChatMessages(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
) : Method<Boolean> {
  public override suspend fun Kotbot.execute(): Boolean = requestForJson(
    name = "unpinAllChatMessages",
    serialize = {
      json.encodeToString(
        serializer(),
        this@UnpinAllChatMessages
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<Boolean>> =
        Response.serializer(Boolean.serializer())
  }
}
