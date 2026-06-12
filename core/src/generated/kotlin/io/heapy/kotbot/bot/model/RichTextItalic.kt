package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An italicized text.
 */
@Serializable
public data class RichTextItalic(
    /**
     * Type of the rich text, always "italic"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
