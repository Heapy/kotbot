package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with a map, corresponding to the custom HTML tag `<tg-map>`.
 */
@Serializable
public data class RichBlockMap(
    /**
     * Type of the block, always "map"
     */
    public val type: String,
    /**
     * Location of the center of the map
     */
    public val location: Location,
    /**
     * Map zoom level; 13-20
     */
    public val zoom: Int,
    /**
     * Expected width of the map
     */
    public val width: Int,
    /**
     * Expected height of the map
     */
    public val height: Int,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
