package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A slideshow, corresponding to the custom HTML tag `<tg-slideshow>`.
 */
@Serializable
public data class RichBlockSlideshow(
    /**
     * Type of the block, always "slideshow"
     */
    public val type: String,
    /**
     * Elements of the slideshow
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
