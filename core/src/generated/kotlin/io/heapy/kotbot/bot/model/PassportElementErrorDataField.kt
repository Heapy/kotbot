package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue in one of the data fields that was provided by the user. The error is considered resolved when the field's value changes.
 */
@Serializable
public data class PassportElementErrorDataField(
  /**
   * Error source, must be *data*
   */
  public val source: String,
  /**
   * The section of the user's Telegram Passport which has the error, one of “personal\_details”, “passport”, “driver\_license”, “identity\_card”, “internal\_passport”, “address”
   */
  public val type: String,
  /**
   * Name of the data field which has the error
   */
  public val field_name: String,
  /**
   * Base64-encoded data hash
   */
  public val data_hash: String,
  /**
   * Error message
   */
  public val message: String,
)
