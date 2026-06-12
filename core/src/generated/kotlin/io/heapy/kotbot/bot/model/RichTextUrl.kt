package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A text with a link.
 */
@Serializable
public data class RichTextUrl(
    /**
     * Type of the rich text, always "url"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * URL of the link
     */
    public val url: String,
) : RichText
