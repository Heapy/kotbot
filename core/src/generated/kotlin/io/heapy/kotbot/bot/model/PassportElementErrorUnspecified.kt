package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue in an unspecified place. The error is considered resolved when new data is added.
 */
@Serializable
public data class PassportElementErrorUnspecified(
  /**
   * Error source, must be *unspecified*
   */
  public val source: String,
  /**
   * Type of element of the user's Telegram Passport which has the issue
   */
  public val type: String,
  /**
   * Base64-encoded element hash
   */
  public val element_hash: String,
  /**
   * Error message
   */
  public val message: String,
) : PassportElementError
