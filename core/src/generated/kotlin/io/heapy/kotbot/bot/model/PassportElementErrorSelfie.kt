package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with the selfie with a document. The error is considered resolved when the file with the selfie changes.
 */
@Serializable
public data class PassportElementErrorSelfie(
  /**
   * Error source, must be *selfie*
   */
  public val source: String,
  /**
   * The section of the user's Telegram Passport which has the issue, one of “passport”, “driver\_license”, “identity\_card”, “internal\_passport”
   */
  public val type: String,
  /**
   * Base64-encoded hash of the file with the selfie
   */
  public val file_hash: String,
  /**
   * Error message
   */
  public val message: String,
)
