package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Thumb
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set the thumbnail of a sticker set. Animated thumbnails can be set for animated sticker sets only. Video thumbnails can be set only for video sticker sets only. Returns *True* on success.
 */
@Serializable
public data class SetStickerSetThumb(
  /**
   * Sticker set name
   */
  public val name: String,
  /**
   * User identifier of the sticker set owner
   */
  public val user_id: Long,
  /**
   * A **PNG** image with the thumbnail, must be up to 128 kilobytes in size and have width and height exactly 100px, or a **TGS** animation with the thumbnail up to 32 kilobytes in size; see [https://core.telegram.org/stickers#animated-sticker-requirements](https://core.telegram.org/stickers#animated-sticker-requirements) for animated sticker technical requirements, or a **WEBM** video with the thumbnail up to 32 kilobytes in size; see [https://core.telegram.org/stickers#video-sticker-requirements](https://core.telegram.org/stickers#video-sticker-requirements) for video sticker technical requirements. Pass a *file_id* as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files). Animated sticker set thumbnails can't be uploaded via HTTP URL.
   */
  public val thumb: Thumb? = null,
) : Method<Boolean> {
  public override suspend fun Kotbot.execute(): Boolean = requestForJson(
    name = "setStickerSetThumb",
    serialize = {
      json.encodeToString(
        serializer(),
        this@SetStickerSetThumb
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
