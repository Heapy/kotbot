package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with the reverse side of a document. The error is considered resolved when the file with reverse side of the document changes.
 */
@Serializable
public data class PassportElementErrorReverseSide(
  /**
   * Error source, must be *reverse_side*
   */
  public val source: String = "reverse_side",
  /**
   * The section of the user's Telegram Passport which has the issue, one of “driver_license”, “identity_card”
   */
  public val type: String,
  /**
   * Base64-encoded hash of the file with the reverse side of the document
   */
  public val file_hash: String,
  /**
   * Error message
   */
  public val message: String,
) : PassportElementError
