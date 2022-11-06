package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a phone contact.
 */
@Serializable
public data class Contact(
    /**
     * Contact's phone number
     */
    public val phone_number: String,
    /**
     * Contact's first name
     */
    public val first_name: String,
    /**
     * *Optional*. Contact's last name
     */
    public val last_name: String? = null,
    /**
     * *Optional*. Contact's user identifier in Telegram. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val user_id: Long? = null,
    /**
     * *Optional*. Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard)
     */
    public val vcard: String? = null,
)
