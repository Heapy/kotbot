package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An underlined text.
 */
@Serializable
public data class RichTextUnderline(
    /**
     * Type of the rich text, always "underline"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
