package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a change in the price of paid messages within a chat.
 */
@Serializable
public data class PaidMessagePriceChanged(
    /**
     * The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each sent message
     */
    public val paid_message_star_count: Int,
)
