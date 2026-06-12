package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A bold text.
 */
@Serializable
public data class RichTextBold(
    /**
     * Type of the rich text, always "bold"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
