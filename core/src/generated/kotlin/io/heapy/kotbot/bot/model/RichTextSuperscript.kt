package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A superscript text.
 */
@Serializable
public data class RichTextSuperscript(
    /**
     * Type of the rich text, always "superscript"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
