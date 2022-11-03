package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.model.SentWebAppMessage
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set the result of an interaction with a [Web App](https://core.telegram.org/bots/webapps) and send a corresponding message on behalf of the user to the chat from which the query originated. On success, a [SentWebAppMessage](https://core.telegram.org/bots/api/#sentwebappmessage) object is returned.
 */
@Serializable
public data class AnswerWebAppQuery(
  /**
   * Unique identifier for the query to be answered
   */
  public val web_app_query_id: String,
  /**
   * A JSON-serialized object describing the message to be sent
   */
  public val result: InlineQueryResult,
) : Method<SentWebAppMessage> {
  public override suspend fun Kotbot.execute(): SentWebAppMessage = requestForJson(
    name = "answerWebAppQuery",
    serialize = {
      json.encodeToString(
        serializer(),
        this@AnswerWebAppQuery
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<SentWebAppMessage>> =
        Response.serializer(SentWebAppMessage.serializer())
  }
}
