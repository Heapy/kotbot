package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents an issue with a list of scans. The error is considered resolved when the list of files containing the scans changes.
 */
@Serializable
public data class PassportElementErrorFiles(
  /**
   * Error source, must be *files*
   */
  public val source: String = "files",
  /**
   * The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
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
