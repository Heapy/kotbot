package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A text with a phone number.
 */
@Serializable
public data class RichTextPhoneNumber(
    /**
     * Type of the rich text, always "phone_number"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The phone number
     */
    public val phone_number: String,
) : RichText
