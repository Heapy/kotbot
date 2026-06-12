package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with a video, corresponding to the HTML tag `<video>`.
 */
@Serializable
public data class RichBlockVideo(
    /**
     * Type of the block, always "video"
     */
    public val type: String,
    /**
     * The video
     */
    public val video: Video,
    /**
     * *Optional*. *True*, if the media preview is covered by a spoiler animation
     */
    public val has_spoiler: Boolean? = null,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
