package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a message about a scheduled giveaway.
 */
@Serializable
public data class Giveaway(
    /**
     * The list of chats which the user must join to participate in the giveaway
     */
    public val chats: List<Chat>,
    /**
     * Point in time (Unix timestamp) when winners of the giveaway will be selected
     */
    public val winners_selection_date: Long,
    /**
     * The number of users which are supposed to be selected as winners of the giveaway
     */
    public val winner_count: Int,
    /**
     * *Optional*. *True*, if only users who join the chats after the giveaway started should be eligible to win
     */
    public val only_new_members: Boolean? = null,
    /**
     * *Optional*. *True*, if the list of giveaway winners will be visible to everyone
     */
    public val has_public_winners: Boolean? = null,
    /**
     * *Optional*. Description of additional giveaway prize
     */
    public val prize_description: String? = null,
    /**
     * *Optional*. A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes indicating the countries from which eligible users for the giveaway must come. If empty, then all users can participate in the giveaway. Users with a phone number that was bought on Fragment can always participate in giveaways.
     */
    public val country_codes: List<String>? = null,
    /**
     * *Optional*. The number of months the Telegram Premium subscription won from the giveaway will be active for
     */
    public val premium_subscription_month_count: Int? = null,
)
