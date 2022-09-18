package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents an issue with the translated version of a document. The error is considered resolved when a file with the document translation change.
 */
@Serializable
public data class PassportElementErrorTranslationFiles(
  /**
   * Error source, must be *translation\_files*
   */
  public val source: String,
  /**
   * Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver\_license”, “identity\_card”, “internal\_passport”, “utility\_bill”, “bank\_statement”, “rental\_agreement”, “passport\_registration”, “temporary\_registration”
   */
  public val type: String,
  /**
   * List of base64-encoded file hashes
   */
  public val file_hashes: List<String>,
  /**
   * Error message
   */
  public val message: String,
) : PassportElementError
