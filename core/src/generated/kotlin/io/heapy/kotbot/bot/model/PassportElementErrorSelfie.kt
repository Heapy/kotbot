package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an issue with the selfie with a document. The error is considered resolved when the file with the selfie changes.
 */
@Serializable
public data class PassportElementErrorSelfie(
    /**
     * Error source, must be *selfie*
     */
    public val source: String = "selfie",
    /**
     * The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”
     */
    public val type: String,
    /**
     * Base64-encoded hash of the file with the selfie
     */
    public val file_hash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError
