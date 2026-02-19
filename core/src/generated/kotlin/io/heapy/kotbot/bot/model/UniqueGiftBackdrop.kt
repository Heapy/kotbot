package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object describes the backdrop of a unique gift.
 */
@Serializable
public data class UniqueGiftBackdrop(
    /**
     * Name of the backdrop
     */
    public val name: String,
    /**
     * Colors of the backdrop
     */
    public val colors: UniqueGiftBackdropColors,
    /**
     * The number of unique gifts that receive this backdrop for every 1000 gifts upgraded
     */
    public val rarity_per_mille: Int,
)
