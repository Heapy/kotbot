package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a story area pointing to an HTTP or tg:// link. Currently, a story can have up to 3 link areas.
 */
@Serializable
public data class StoryAreaTypeLink(
    /**
     * Type of the area, always "link"
     */
    public val type: String,
    /**
     * HTTP or tg:// URL to be opened when the area is clicked
     */
    public val url: String,
) : StoryAreaType
