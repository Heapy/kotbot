package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a change in auto-delete timer settings.
 */
@Serializable
public data class MessageAutoDeleteTimerChanged(
  /**
   * New auto-delete time for messages in the chat; in seconds
   */
  public val message_auto_delete_time: Int,
)
