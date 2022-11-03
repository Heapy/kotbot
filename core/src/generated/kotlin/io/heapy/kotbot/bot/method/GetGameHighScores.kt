package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.GameHighScore
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get data for high score tables. Will return the score of the specified user and several of their neighbors in a game. Returns an Array of [GameHighScore](https://core.telegram.org/bots/api/#gamehighscore) objects.
 *
 * This method will currently return scores for the target user, plus two of their closest neighbors on each side. Will also return the top three users if the user and their neighbors are not among them. Please note that this behavior is subject to change.
 */
@Serializable
public data class GetGameHighScores(
  /**
   * Target user id
   */
  public val user_id: Long,
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
) : Method<List<GameHighScore>> {
  public override suspend fun Kotbot.execute(): List<GameHighScore> = requestForJson(
    name = "getGameHighScores",
    serialize = {
      json.encodeToString(
        serializer(),
        this@GetGameHighScores
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<List<GameHighScore>>> =
        Response.serializer(ListSerializer(GameHighScore.serializer()))
  }
}
