package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object describes the model of a unique gift.
 */
@Serializable
public data class UniqueGiftModel(
    /**
     * Name of the model
     */
    public val name: String,
    /**
     * The sticker that represents the unique gift
     */
    public val sticker: Sticker,
    /**
     * The number of unique gifts that receive this model for every 1000 gift upgrades. Always 0 for crafted gifts.
     */
    public val rarity_per_mille: Int,
    /**
     * *Optional*. Rarity of the model if it is a crafted model. Currently, can be "uncommon", "rare", "epic", or "legendary".
     */
    public val rarity: String? = null,
)
