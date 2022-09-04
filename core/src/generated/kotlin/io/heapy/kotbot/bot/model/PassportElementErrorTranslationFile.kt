package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with one of the files that constitute the translation of a document. The error is considered resolved when the file changes.
 */
@Serializable
public data class PassportElementErrorTranslationFile(
  /**
   * Error source, must be *translation\_file*
   */
  public val source: String,
  /**
   * Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver\_license”, “identity\_card”, “internal\_passport”, “utility\_bill”, “bank\_statement”, “rental\_agreement”, “passport\_registration”, “temporary\_registration”
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
)
