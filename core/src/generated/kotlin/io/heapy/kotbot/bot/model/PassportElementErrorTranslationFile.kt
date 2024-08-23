package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with one of the files that constitute the translation of a document. The error is considered resolved when the file changes.
 */
@Serializable
public data class PassportElementErrorTranslationFile(
    /**
     * Error source, must be *translation_file*
     */
    public val source: String,
    /**
     * Type of element of the user's Telegram Passport which has the issue, one of "passport", "driver_license", "identity_card", "internal_passport", "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration"
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
