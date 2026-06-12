package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A divider, corresponding to the HTML tag `<hr/>`.
 */
@Serializable
public data class RichBlockDivider(
    /**
     * Type of the block, always "divider"
     */
    public val type: String,
) : RichBlock
