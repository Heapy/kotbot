package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatMember
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of [ChatMember](https://core.telegram.org/bots/api/#chatmember) objects.
 */
@Serializable
public data class GetChatAdministrators(
  /**
   * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
) : Method<List<ChatMember>> {
  public override suspend fun Kotbot.execute(): List<ChatMember> = requestForJson(
    name = "getChatAdministrators",
    serialize = {
      json.encodeToString(
        serializer(),
        this@GetChatAdministrators
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<List<ChatMember>>> =
        Response.serializer(ListSerializer(ChatMember.serializer()))
  }
}
