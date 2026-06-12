package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A strikethrough text.
 */
@Serializable
public data class RichTextStrikethrough(
    /**
     * Type of the rich text, always "strikethrough"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
