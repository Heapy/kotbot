package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with an animation, corresponding to the HTML tag `<video>`.
 */
@Serializable
public data class RichBlockAnimation(
    /**
     * Type of the block, always "animation"
     */
    public val type: String,
    /**
     * The animation
     */
    public val animation: Animation,
    /**
     * *Optional*. *True*, if the media preview is covered by a spoiler animation
     */
    public val has_spoiler: Boolean? = null,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
