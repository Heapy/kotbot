package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to move a sticker in a set created by the bot to a specific position. Returns *True* on success.
 */
@Serializable
public data class SetStickerPositionInSet(
  /**
   * File identifier of the sticker
   */
  public val sticker: String,
  /**
   * New sticker position in the set, zero-based
   */
  public val position: Int,
) : Method<Boolean> {
  public override suspend fun Kotbot.execute(): Boolean = requestForJson(
    name = "setStickerPositionInSet",
    serialize = {
      json.encodeToString(
        serializer(),
        this@SetStickerPositionInSet
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
