package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a message about the completion of a giveaway with public winners.
 */
@Serializable
public data class GiveawayWinners(
    /**
     * The chat that created the giveaway
     */
    public val chat: Chat,
    /**
     * Identifier of the message with the giveaway in the chat
     */
    public val giveaway_message_id: Int,
    /**
     * Point in time (Unix timestamp) when winners of the giveaway were selected
     */
    public val winners_selection_date: Long,
    /**
     * Total number of winners in the giveaway
     */
    public val winner_count: Int,
    /**
     * List of up to 100 winners of the giveaway
     */
    public val winners: List<User>,
    /**
     * *Optional*. The number of other chats the user had to join in order to be eligible for the giveaway
     */
    public val additional_chat_count: Int? = null,
    /**
     * *Optional*. The number of Telegram Stars that were split between giveaway winners; for Telegram Star giveaways only
     */
    public val prize_star_count: Int? = null,
    /**
     * *Optional*. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
     */
    public val premium_subscription_month_count: Int? = null,
    /**
     * *Optional*. Number of undistributed prizes
     */
    public val unclaimed_prize_count: Int? = null,
    /**
     * *Optional*. *True*, if only users who had joined the chats after the giveaway started were eligible to win
     */
    public val only_new_members: Boolean? = null,
    /**
     * *Optional*. *True*, if the giveaway was canceled because the payment for it was refunded
     */
    public val was_refunded: Boolean? = null,
    /**
     * *Optional*. Description of additional giveaway prize
     */
    public val prize_description: String? = null,
)
