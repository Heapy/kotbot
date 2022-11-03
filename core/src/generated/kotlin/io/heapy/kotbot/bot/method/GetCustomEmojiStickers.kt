package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Sticker
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
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
 */
@Serializable
public data class GetCustomEmojiStickers(
  /**
   * List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
   */
  public val custom_emoji_ids: List<String>,
) : Method<List<Sticker>> {
  public override suspend fun Kotbot.execute(): List<Sticker> = requestForJson(
    name = "getCustomEmojiStickers",
    serialize = {
      json.encodeToString(
        serializer(),
        this@GetCustomEmojiStickers
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<List<Sticker>>> =
        Response.serializer(ListSerializer(Sticker.serializer()))
  }
}
