package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A quotation with centered text, loosely corresponding to the HTML tag `<aside>`.
 */
@Serializable
public data class RichBlockPullQuotation(
    /**
     * Type of the block, always "pullquote"
     */
    public val type: String,
    /**
     * Text of the block
     */
    public val text: RichText,
    /**
     * *Optional*. Credit of the block
     */
    public val credit: RichText? = null,
) : RichBlock
