package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A text with an email address.
 */
@Serializable
public data class RichTextEmailAddress(
    /**
     * Type of the rich text, always "email_address"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The email address
     */
    public val email_address: String,
) : RichText
