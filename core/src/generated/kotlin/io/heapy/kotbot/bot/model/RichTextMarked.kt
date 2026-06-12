package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A marked text.
 */
@Serializable
public data class RichTextMarked(
    /**
     * Type of the rich text, always "marked"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
