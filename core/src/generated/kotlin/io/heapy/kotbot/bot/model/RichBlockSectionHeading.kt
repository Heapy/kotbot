package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A section heading, corresponding to the HTML tags `<h1>`, `<h2>`, `<h3>`, `<h4>`, `<h5>`, or `<h6>`.
 */
@Serializable
public data class RichBlockSectionHeading(
    /**
     * Type of the block, always "heading"
     */
    public val type: String,
    /**
     * Text of the block
     */
    public val text: RichText,
    /**
     * Relative size of the text font; 1-6, 1 is the largest, 6 is the smallest
     */
    public val size: Int,
) : RichBlock
