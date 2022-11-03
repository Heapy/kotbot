package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.MessageOrTrue
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to edit live location messages. A location can be edited until its *live_period* expires or editing is explicitly disabled by a call to [stopMessageLiveLocation](https://core.telegram.org/bots/api/#stopmessagelivelocation). On success, if the edited message is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned.
 */
@Serializable
public data class EditMessageLiveLocation(
  /**
   * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId? = null,
  /**
   * Required if *inline_message_id* is not specified. Identifier of the message to edit
   */
  public val message_id: Int? = null,
  /**
   * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
   */
  public val inline_message_id: String? = null,
  /**
   * Latitude of new location
   */
  public val latitude: Double,
  /**
   * Longitude of new location
   */
  public val longitude: Double,
  /**
   * The radius of uncertainty for the location, measured in meters; 0-1500
   */
  public val horizontal_accuracy: Double? = null,
  /**
   * Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
   */
  public val heading: Int? = null,
  /**
   * The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
   */
  public val proximity_alert_radius: Int? = null,
  /**
   * A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating).
   */
  public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<MessageOrTrue> {
  public override suspend fun Kotbot.execute(): MessageOrTrue = requestForJson(
    name = "editMessageLiveLocation",
    serialize = {
      json.encodeToString(
        serializer(),
        this@EditMessageLiveLocation
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<MessageOrTrue>> =
        Response.serializer(MessageOrTrue.serializer())
  }
}
