package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes documents or other Telegram Passport elements shared with the bot by the user.
 */
@Serializable
public data class EncryptedPassportElement(
  /**
   * Element type. One of “personal\_details”, “passport”, “driver\_license”, “identity\_card”, “internal\_passport”, “address”, “utility\_bill”, “bank\_statement”, “rental\_agreement”, “passport\_registration”, “temporary\_registration”, “phone\_number”, “email”.
   */
  public val type: String,
  /**
   * *Optional*. Base64-encoded encrypted Telegram Passport element data provided by the user, available for “personal\_details”, “passport”, “driver\_license”, “identity\_card”, “internal\_passport” and “address” types. Can be decrypted and verified using the accompanying [EncryptedCredentials](https://core.telegram.org/bots/api/#encryptedcredentials).
   */
  public val `data`: String? = null,
  /**
   * *Optional*. User's verified phone number, available only for “phone\_number” type
   */
  public val phone_number: String? = null,
  /**
   * *Optional*. User's verified email address, available only for “email” type
   */
  public val email: String? = null,
  /**
   * *Optional*. Array of encrypted files with documents provided by the user, available for “utility\_bill”, “bank\_statement”, “rental\_agreement”, “passport\_registration” and “temporary\_registration” types. Files can be decrypted and verified using the accompanying [EncryptedCredentials](https://core.telegram.org/bots/api/#encryptedcredentials).
   */
  public val files: List<PassportFile>? = null,
  /**
   * *Optional*. Encrypted file with the front side of the document, provided by the user. Available for “passport”, “driver\_license”, “identity\_card” and “internal\_passport”. The file can be decrypted and verified using the accompanying [EncryptedCredentials](https://core.telegram.org/bots/api/#encryptedcredentials).
   */
  public val front_side: PassportFile? = null,
  /**
   * *Optional*. Encrypted file with the reverse side of the document, provided by the user. Available for “driver\_license” and “identity\_card”. The file can be decrypted and verified using the accompanying [EncryptedCredentials](https://core.telegram.org/bots/api/#encryptedcredentials).
   */
  public val reverse_side: PassportFile? = null,
  /**
   * *Optional*. Encrypted file with the selfie of the user holding a document, provided by the user; available for “passport”, “driver\_license”, “identity\_card” and “internal\_passport”. The file can be decrypted and verified using the accompanying [EncryptedCredentials](https://core.telegram.org/bots/api/#encryptedcredentials).
   */
  public val selfie: PassportFile? = null,
  /**
   * *Optional*. Array of encrypted files with translated versions of documents provided by the user. Available if requested for “passport”, “driver\_license”, “identity\_card”, “internal\_passport”, “utility\_bill”, “bank\_statement”, “rental\_agreement”, “passport\_registration” and “temporary\_registration” types. Files can be decrypted and verified using the accompanying [EncryptedCredentials](https://core.telegram.org/bots/api/#encryptedcredentials).
   */
  public val translation: List<PassportFile>? = null,
  /**
   * Base64-encoded element hash for using in [PassportElementErrorUnspecified](https://core.telegram.org/bots/api/#passportelementerrorunspecified)
   */
  public val hash: String,
)
