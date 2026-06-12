package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * An expandable block for details disclosure, corresponding to the HTML tag `<details>`.
 */
@Serializable
public data class RichBlockDetails(
    /**
     * Type of the block, always "details"
     */
    public val type: String,
    /**
     * Always shown summary of the block
     */
    public val summary: RichText,
    /**
     * Content of the block
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. *True*, if the content of the block is visible by default
     */
    public val is_open: Boolean? = null,
) : RichBlock
