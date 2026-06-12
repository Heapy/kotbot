package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A mention of a Telegram user by their identifier.
 */
@Serializable
public data class RichTextTextMention(
    /**
     * Type of the rich text, always "text_mention"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The mentioned user
     */
    public val user: User,
) : RichText
