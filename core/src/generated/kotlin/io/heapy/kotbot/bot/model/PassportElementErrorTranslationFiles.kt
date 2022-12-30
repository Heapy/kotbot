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
     * Error source, must be *translation_files*
     */
    public val source: String = "translation_files",
    /**
     * Type of element of the user's Telegram Passport which has the issue, one of "passport", "driver_license", "identity_card", "internal_passport", "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration"
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
