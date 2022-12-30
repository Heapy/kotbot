package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with the front side of a document. The error is considered resolved when the file with the front side of the document changes.
 */
@Serializable
public data class PassportElementErrorFrontSide(
    /**
     * Error source, must be *front_side*
     */
    public val source: String = "front_side",
    /**
     * The section of the user's Telegram Passport which has the issue, one of "passport", "driver_license", "identity_card", "internal_passport"
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
) : PassportElementError
