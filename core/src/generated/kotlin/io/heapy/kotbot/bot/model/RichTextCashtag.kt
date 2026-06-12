package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A cashtag.
 */
@Serializable
public data class RichTextCashtag(
    /**
     * Type of the rich text, always "cashtag"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The cashtag
     */
    public val cashtag: String,
) : RichText
