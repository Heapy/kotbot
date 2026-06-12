package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with an anchor, corresponding to the HTML tag `<a>` with the attribute `name`.
 */
@Serializable
public data class RichBlockAnchor(
    /**
     * Type of the block, always "anchor"
     */
    public val type: String,
    /**
     * The name of the anchor
     */
    public val name: String,
) : RichBlock
