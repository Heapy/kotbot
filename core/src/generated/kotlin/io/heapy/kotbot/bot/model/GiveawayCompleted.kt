package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the completion of a giveaway without public winners.
 */
@Serializable
public data class GiveawayCompleted(
    /**
     * Number of winners in the giveaway
     */
    public val winner_count: Int,
    /**
     * *Optional*. Number of undistributed prizes
     */
    public val unclaimed_prize_count: Int? = null,
    /**
     * *Optional*. Message with the giveaway that was completed, if it wasn't deleted
     */
    public val giveaway_message: Message? = null,
    /**
     * *Optional*. *True*, if the giveaway is a Telegram Star giveaway. Otherwise, currently, the giveaway is a Telegram Premium giveaway.
     */
    public val is_star_giveaway: Boolean? = null,
)
