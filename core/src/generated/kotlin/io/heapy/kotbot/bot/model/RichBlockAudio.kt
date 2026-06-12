package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with a music file, corresponding to the HTML tag `<audio>`.
 */
@Serializable
public data class RichBlockAudio(
    /**
     * Type of the block, always "audio"
     */
    public val type: String,
    /**
     * The audio
     */
    public val audio: Audio,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
