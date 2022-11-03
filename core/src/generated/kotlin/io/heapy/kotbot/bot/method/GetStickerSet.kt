package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.StickerSet
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get a sticker set. On success, a [StickerSet](https://core.telegram.org/bots/api/#stickerset) object is returned.
 */
@Serializable
public data class GetStickerSet(
  /**
   * Name of the sticker set
   */
  public val name: String,
) : Method<StickerSet> {
  public override suspend fun Kotbot.execute(): StickerSet = requestForJson(
    name = "getStickerSet",
    serialize = {
      json.encodeToString(
        serializer(),
        this@GetStickerSet
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<StickerSet>> =
        Response.serializer(StickerSet.serializer())
  }
}
