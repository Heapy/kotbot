package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A collage, corresponding to the custom HTML tag `<tg-collage>`.
 */
@Serializable
public data class RichBlockCollage(
    /**
     * Type of the block, always "collage"
     */
    public val type: String,
    /**
     * Elements of the collage
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
