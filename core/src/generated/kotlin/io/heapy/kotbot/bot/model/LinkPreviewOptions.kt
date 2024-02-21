package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes the options used for link preview generation.
 */
@Serializable
public data class LinkPreviewOptions(
    /**
     * *Optional*. *True*, if the link preview is disabled
     */
    public val is_disabled: Boolean? = null,
    /**
     * *Optional*. URL to use for the link preview. If empty, then the first URL found in the message text will be used
     */
    public val url: String? = null,
    /**
     * *Optional*. *True*, if the media in the link preview is supposed to be shrunk; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
     */
    public val prefer_small_media: Boolean? = null,
    /**
     * *Optional*. *True*, if the media in the link preview is supposed to be enlarged; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
     */
    public val prefer_large_media: Boolean? = null,
    /**
     * *Optional*. *True*, if the link preview must be shown above the message text; otherwise, the link preview will be shown below the message text
     */
    public val show_above_text: Boolean? = null,
)
