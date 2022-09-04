package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents one row of the high scores table for a game.
 */
@Serializable
public data class GameHighScore(
  /**
   * Position in high score table for the game
   */
  public val position: Int,
  /**
   * User
   */
  public val user: User,
  /**
   * Score
   */
  public val score: Int,
)
