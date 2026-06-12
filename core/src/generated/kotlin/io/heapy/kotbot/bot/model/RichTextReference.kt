package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A reference.
 */
@Serializable
public data class RichTextReference(
    /**
     * Type of the rich text, always "reference"
     */
    public val type: String,
    /**
     * Text of the reference
     */
    public val text: RichText,
    /**
     * The name of the reference
     */
    public val name: String,
) : RichText
