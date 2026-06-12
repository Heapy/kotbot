package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A text paragraph, corresponding to the HTML tag `<p>`.
 */
@Serializable
public data class RichBlockParagraph(
    /**
     * Type of the block, always "paragraph"
     */
    public val type: String,
    /**
     * Text of the block
     */
    public val text: RichText,
) : RichBlock
