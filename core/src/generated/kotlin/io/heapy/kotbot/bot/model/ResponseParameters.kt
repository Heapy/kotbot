package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Describes why a request was unsuccessful.
 */
@Serializable
public data class ResponseParameters(
  /**
   * *Optional*. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
   */
  public val migrate_to_chat_id: Long? = null,
  /**
   * *Optional*. In case of exceeding flood control, the number of seconds left to wait before the request can be repeated
   */
  public val retry_after: Int? = null,
)
