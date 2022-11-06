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
    public val source: String = "file",
    /**
     * The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
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
