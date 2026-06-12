package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A hashtag.
 */
@Serializable
public data class RichTextHashtag(
    /**
     * Type of the rich text, always "hashtag"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The hashtag
     */
    public val hashtag: String,
) : RichText
