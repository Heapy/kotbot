package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A block quotation, corresponding to the HTML tag `<blockquote>`.
 */
@Serializable
public data class RichBlockBlockQuotation(
    /**
     * Type of the block, always "blockquote"
     */
    public val type: String,
    /**
     * Content of the block
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. Credit of the block
     */
    public val credit: RichText? = null,
) : RichBlock
