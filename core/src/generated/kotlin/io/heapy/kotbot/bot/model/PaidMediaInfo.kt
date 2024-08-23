package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes the paid media added to a message.
 */
@Serializable
public data class PaidMediaInfo(
    /**
     * The number of Telegram Stars that must be paid to buy access to the media
     */
    public val star_count: Int,
    /**
     * Information about the paid media
     */
    public val paid_media: List<PaidMedia>,
)
