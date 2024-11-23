package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the creation of a scheduled giveaway.
 */
@Serializable
public data class GiveawayCreated(
    /**
     * *Optional*. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
     */
    public val prize_star_count: Int? = null,
)
