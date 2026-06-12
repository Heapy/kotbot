package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A mention by a username.
 */
@Serializable
public data class RichTextMention(
    /**
     * Type of the rich text, always "mention"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The username
     */
    public val username: String,
) : RichText
