package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A link to an anchor.
 */
@Serializable
public data class RichTextAnchorLink(
    /**
     * Type of the rich text, always "anchor_link"
     */
    public val type: String,
    /**
     * The link text
     */
    public val text: RichText,
    /**
     * The name of the anchor. If the name is empty, then the link brings back to the top of the message.
     */
    public val anchor_name: String,
) : RichText
