package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with a document scan. The error is considered resolved when the file with the document scan changes.
 */
@Serializable
public data class PassportElementErrorFile(
  /**
   * Error source, must be *file*
   */
  public val source: String,
  /**
   * The section of the user's Telegram Passport which has the issue, one of “utility\_bill”, “bank\_statement”, “rental\_agreement”, “passport\_registration”, “temporary\_registration”
   */
  public val type: String,
  /**
   * Base64-encoded file hash
   */
  public val file_hash: String,
  /**
   * Error message
   */
  public val message: String,
) : PassportElementError
