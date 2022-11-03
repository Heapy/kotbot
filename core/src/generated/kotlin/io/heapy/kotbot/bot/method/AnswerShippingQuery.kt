package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ShippingOption
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * If you sent an invoice requesting a shipping address and the parameter *is_flexible* was specified, the Bot API will send an [Update](https://core.telegram.org/bots/api/#update) with a *shipping_query* field to the bot. Use this method to reply to shipping queries. On success, *True* is returned.
 */
@Serializable
public data class AnswerShippingQuery(
  /**
   * Unique identifier for the query to be answered
   */
  public val shipping_query_id: String,
  /**
   * Pass *True* if delivery to the specified address is possible and *False* if there are any problems (for example, if delivery to the specified address is not possible)
   */
  public val ok: Boolean,
  /**
   * Required if *ok* is *True*. A JSON-serialized array of available shipping options.
   */
  public val shipping_options: List<ShippingOption>? = null,
  /**
   * Required if *ok* is *False*. Error message in human readable form that explains why it is impossible to complete the order (e.g. "Sorry, delivery to your desired address is unavailable'). Telegram will display this message to the user.
   */
  public val error_message: String? = null,
) : Method<Boolean> {
  public override suspend fun Kotbot.execute(): Boolean = requestForJson(
    name = "answerShippingQuery",
    serialize = {
      json.encodeToString(
        serializer(),
        this@AnswerShippingQuery
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
