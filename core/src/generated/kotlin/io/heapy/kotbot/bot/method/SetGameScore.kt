package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MessageOrTrue
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set the score of the specified user in a game message. On success, if the message is not an inline message, the [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned. Returns an error, if the new score is not greater than the user's current score in the chat and *force* is *False*.
 */
@Serializable
public data class SetGameScore(
  /**
   * User identifier
   */
  public val user_id: Long,
  /**
   * New score, must be non-negative
   */
  public val score: Int,
  /**
   * Pass *True* if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters
   */
  public val force: Boolean? = null,
  /**
   * Pass *True* if the game message should not be automatically edited to include the current scoreboard
   */
  public val disable_edit_message: Boolean? = null,
  /**
   * Required if *inline_message_id* is not specified. Unique identifier for the target chat
   */
  public val chat_id: Long? = null,
  /**
   * Required if *inline_message_id* is not specified. Identifier of the sent message
   */
  public val message_id: Int? = null,
  /**
   * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
   */
  public val inline_message_id: String? = null,
) : Method<MessageOrTrue> {
  public override suspend fun Kotbot.execute(): MessageOrTrue = requestForJson(
    name = "setGameScore",
    serialize = {
      json.encodeToString(
        serializer(),
        this@SetGameScore
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