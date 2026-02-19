package io.heapy.kotbot.bot.model

import kotlin.Boolean
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
     * *Optional*. *True*, if the gift can only be purchased by Telegram Premium subscribers
     */
    public val is_premium: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be used (after being upgraded) to customize a user's appearance
     */
    public val has_colors: Boolean? = null,
    /**
     * *Optional*. The total number of gifts of this type that can be sent by all users; for limited gifts only
     */
    public val total_count: Int? = null,
    /**
     * *Optional*. The number of remaining gifts of this type that can be sent by all users; for limited gifts only
     */
    public val remaining_count: Int? = null,
    /**
     * *Optional*. The total number of gifts of this type that can be sent by the bot; for limited gifts only
     */
    public val personal_total_count: Int? = null,
    /**
     * *Optional*. The number of remaining gifts of this type that can be sent by the bot; for limited gifts only
     */
    public val personal_remaining_count: Int? = null,
    /**
     * *Optional*. Background of the gift
     */
    public val background: GiftBackground? = null,
    /**
     * *Optional*. The total number of different unique gifts that can be obtained by upgrading the gift
     */
    public val unique_gift_variant_count: Int? = null,
    /**
     * *Optional*. Information about the chat that published the gift
     */
    public val publisher_chat: Chat? = null,
)
