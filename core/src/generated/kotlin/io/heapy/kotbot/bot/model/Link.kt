package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an HTTP link.
 */
@Serializable
public data class Link(
    /**
     * URL of the link
     */
    public val url: String,
)
