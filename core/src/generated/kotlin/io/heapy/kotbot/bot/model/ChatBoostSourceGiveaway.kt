package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The boost was obtained by the creation of a Telegram Premium or a Telegram Star giveaway. This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription for Telegram Premium giveaways and *prize_star_count* / 500 times for one year for Telegram Star giveaways.
 */
@Serializable
public data class ChatBoostSourceGiveaway(
    /**
     * Source of the boost, always "giveaway"
     */
    public val source: String,
    /**
     * Identifier of a message in the chat with the giveaway; the message could have been deleted already. May be 0 if the message isn't sent yet.
     */
    public val giveaway_message_id: Int,
    /**
     * *Optional*. User that won the prize in the giveaway if any; for Telegram Premium giveaways only
     */
    public val user: User? = null,
    /**
     * *Optional*. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
     */
    public val prize_star_count: Int? = null,
    /**
     * *Optional*. *True*, if the giveaway was completed, but there was no user to win the prize
     */
    public val is_unclaimed: Boolean? = null,
) : ChatBoostSource
