package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns *True* on success.
 *
 * Example: The [ImageBot](https://t.me/imagebot) needs some time to process a request and upload the image. Instead of sending a text message along the lines of “Retrieving image, please wait…”, the bot may use [sendChatAction](https://core.telegram.org/bots/api/#sendchataction) with *action* = *upload_photo*. The user will see a “sending photo” status for the bot.
 *
 * We only recommend using this method when a response from the bot will take a **noticeable** amount of time to arrive.
 */
@Serializable
public data class SendChatAction(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Type of action to broadcast. Choose one, depending on what the user is about to receive: *typing* for [text messages](https://core.telegram.org/bots/api/#sendmessage), *upload_photo* for [photos](https://core.telegram.org/bots/api/#sendphoto), *record_video* or *upload_video* for [videos](https://core.telegram.org/bots/api/#sendvideo), *record_voice* or *upload_voice* for [voice notes](https://core.telegram.org/bots/api/#sendvoice), *upload_document* for [general files](https://core.telegram.org/bots/api/#senddocument), *choose_sticker* for [stickers](https://core.telegram.org/bots/api/#sendsticker), *find_location* for [location data](https://core.telegram.org/bots/api/#sendlocation), *record_video_note* or *upload_video_note* for [video notes](https://core.telegram.org/bots/api/#sendvideonote).
   */
  public val action: String,
) : Method<Boolean> {
  public override suspend fun Kotbot.execute(): Boolean = requestForJson(
    name = "sendChatAction",
    serialize = {
      json.encodeToString(
        serializer(),
        this@SendChatAction
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
