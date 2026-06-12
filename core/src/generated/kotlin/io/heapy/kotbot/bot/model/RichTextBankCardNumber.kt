package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A text with a bank card number.
 */
@Serializable
public data class RichTextBankCardNumber(
    /**
     * Type of the rich text, always "bank_card_number"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The bank card number
     */
    public val bank_card_number: String,
) : RichText
