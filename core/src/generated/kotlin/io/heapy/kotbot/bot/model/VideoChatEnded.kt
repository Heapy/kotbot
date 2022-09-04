package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a video chat ended in the chat.
 */
@Serializable
public data class VideoChatEnded(
  /**
   * Video chat duration in seconds
   */
  public val duration: Int,
)
