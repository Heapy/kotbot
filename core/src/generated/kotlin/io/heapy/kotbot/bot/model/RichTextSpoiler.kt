package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A text covered by a spoiler.
 */
@Serializable
public data class RichTextSpoiler(
    /**
     * Type of the rich text, always "spoiler"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
