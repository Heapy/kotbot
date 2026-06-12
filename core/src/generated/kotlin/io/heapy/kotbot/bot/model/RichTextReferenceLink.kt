package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A link to a reference.
 */
@Serializable
public data class RichTextReferenceLink(
    /**
     * Type of the rich text, always "reference_link"
     */
    public val type: String,
    /**
     * The link text
     */
    public val text: RichText,
    /**
     * The name of the reference
     */
    public val reference_name: String,
) : RichText
