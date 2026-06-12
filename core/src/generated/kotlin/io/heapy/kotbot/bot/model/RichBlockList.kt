package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A list of blocks, corresponding to the HTML tag `<ul>` or `<ol>` with multiple nested tags `<li>`.
 */
@Serializable
public data class RichBlockList(
    /**
     * Type of the block, always "list"
     */
    public val type: String,
    /**
     * Items of the list
     */
    public val items: List<RichBlockListItem>,
) : RichBlock
