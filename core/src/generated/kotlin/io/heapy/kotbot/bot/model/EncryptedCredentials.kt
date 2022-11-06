package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes data required for decrypting and authenticating [EncryptedPassportElement](https://core.telegram.org/bots/api/#encryptedpassportelement). See the [Telegram Passport Documentation](https://core.telegram.org/passport#receiving-information) for a complete description of the data decryption and authentication processes.
 */
@Serializable
public data class EncryptedCredentials(
    /**
     * Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required for [EncryptedPassportElement](https://core.telegram.org/bots/api/#encryptedpassportelement) decryption and authentication
     */
    public val `data`: String,
    /**
     * Base64-encoded data hash for data authentication
     */
    public val hash: String,
    /**
     * Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption
     */
    public val secret: String,
)
