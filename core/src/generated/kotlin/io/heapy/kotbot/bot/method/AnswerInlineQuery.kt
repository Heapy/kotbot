package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to send answers to an inline query. On success, *True* is returned.  
 * No more than **50** results per query are allowed.
 */
@Serializable
public data class AnswerInlineQuery(
  /**
   * Unique identifier for the answered query
   */
  public val inline_query_id: String,
  /**
   * A JSON-serialized array of results for the inline query
   */
  public val results: List<InlineQueryResult>,
  /**
   * The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
   */
  public val cache_time: Int? = 300,
  /**
   * Pass *True* if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query
   */
  public val is_personal: Boolean? = null,
  /**
   * Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
   */
  public val next_offset: String? = null,
  /**
   * If passed, clients will display a button with specified text that switches the user to a private chat with the bot and sends the bot a start message with the parameter *switch_pm_parameter*
   */
  public val switch_pm_text: String? = null,
  /**
   * [Deep-linking](https://core.telegram.org/bots#deep-linking) parameter for the /start message sent to the bot when user presses the switch button. 1-64 characters, only `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.  
   *
   * *Example:* An inline bot that sends YouTube videos can ask the user to connect the bot to their YouTube account to adapt search results accordingly. To do this, it displays a 'Connect your YouTube account' button above the results, or even before showing any. The user presses the button, switches to a private chat with the bot and, in doing so, passes a start parameter that instructs the bot to return an OAuth link. Once done, the bot can offer a [*switch_inline*](https://core.telegram.org/bots/api/#inlinekeyboardmarkup) button so that the user can easily return to the chat where they wanted to use the bot's inline capabilities.
   */
  public val switch_pm_parameter: String? = null,
) : Method<Boolean> {
  public override suspend fun Kotbot.execute(): Boolean = requestForJson(
    name = "answerInlineQuery",
    serialize = {
      json.encodeToString(
        serializer(),
        this@AnswerInlineQuery
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
