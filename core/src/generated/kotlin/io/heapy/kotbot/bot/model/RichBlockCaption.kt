package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * Caption of a rich formatted block.
 */
@Serializable
public data class RichBlockCaption(
    /**
     * Block caption
     */
    public val text: RichText,
    /**
     * *Optional*. Block credit which corresponds to the HTML tag <cite>
     */
    public val credit: RichText? = null,
)
