package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with the front side of a document. The error is considered resolved when the file with the front side of the document changes.
 */
@Serializable
public data class PassportElementErrorFrontSide(
  /**
   * Error source, must be *front\_side*
   */
  public val source: String,
  /**
   * The section of the user's Telegram Passport which has the issue, one of “passport”, “driver\_license”, “identity\_card”, “internal\_passport”
   */
  public val type: String,
  /**
   * Base64-encoded hash of the file with the front side of the document
   */
  public val file_hash: String,
  /**
   * Error message
   */
  public val message: String,
)
