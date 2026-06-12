package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A monowidth text.
 */
@Serializable
public data class RichTextCode(
    /**
     * Type of the rich text, always "code"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
) : RichText
