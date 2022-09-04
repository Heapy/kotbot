package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 */
@Serializable
public data class PollOption(
  /**
   * Option text, 1-100 characters
   */
  public val text: String,
  /**
   * Number of users that voted for this option
   */
  public val voter_count: Int,
)
