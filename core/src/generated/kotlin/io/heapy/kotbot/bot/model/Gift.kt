package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a gift that can be sent by the bot.
 */
@Serializable
public data class Gift(
    /**
     * Unique identifier of the gift
     */
    public val id: String,
    /**
     * The sticker that represents the gift
     */
    public val sticker: Sticker,
    /**
     * The number of Telegram Stars that must be paid to send the sticker
     */
    public val star_count: Int,
    /**
     * *Optional*. The number of Telegram Stars that must be paid to upgrade the gift to a unique one
     */
    public val upgrade_star_count: Int? = null,
    /**
     * *Optional*. The total number of the gifts of this type that can be sent; for limited gifts only
     */
    public val total_count: Int? = null,
    /**
     * *Optional*. The number of remaining gifts of this type that can be sent; for limited gifts only
     */
    public val remaining_count: Int? = null,
)
