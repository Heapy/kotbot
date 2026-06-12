package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An anchor.
 */
@Serializable
public data class RichTextAnchor(
    /**
     * Type of the rich text, always "anchor"
     */
    public val type: String,
    /**
     * The name of the anchor
     */
    public val name: String,
) : RichText
