package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a story area pointing to a unique gift. Currently, a story can have at most 1 unique gift area.
 */
@Serializable
public data class StoryAreaTypeUniqueGift(
    /**
     * Type of the area, always "unique_gift"
     */
    public val type: String,
    /**
     * Unique name of the gift
     */
    public val name: String,
) : StoryAreaType
