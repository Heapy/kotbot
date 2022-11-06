package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the [content](https://core.telegram.org/bots/api/#inputmessagecontent) of a contact message to be sent as the result of an inline query.
 */
@Serializable
public data class InputContactMessageContent(
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
     * *Optional*. Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
     */
    public val vcard: String? = null,
) : InputMessageContent
