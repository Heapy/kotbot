package io.heapy.kotbot.bot.method

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

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
)
