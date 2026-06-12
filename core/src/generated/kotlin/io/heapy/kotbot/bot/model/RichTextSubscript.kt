package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A subscript text.
 */
@Serializable
public data class RichTextSubscript(
    /**
     * Type of the rich text, always "subscript"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
