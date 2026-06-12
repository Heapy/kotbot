package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A block with a photo, corresponding to the HTML tag `<photo>`.
 */
@Serializable
public data class RichBlockPhoto(
    /**
     * Type of the block, always "photo"
     */
    public val type: String,
    /**
     * Available sizes of the photo
     */
    public val photo: List<PhotoSize>,
    /**
     * *Optional*. *True*, if the media preview is covered by a spoiler animation
     */
    public val has_spoiler: Boolean? = null,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
