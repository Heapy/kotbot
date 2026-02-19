package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes the price of a suggested post.
 */
@Serializable
public data class SuggestedPostPrice(
    /**
     * Currency in which the post will be paid. Currently, must be one of "XTR" for Telegram Stars or "TON" for toncoins
     */
    public val currency: String,
    /**
     * The amount of the currency that will be paid for the post in the *smallest units* of the currency, i.e. Telegram Stars or nanotoncoins. Currently, price in Telegram Stars must be between 5 and 100000, and price in nanotoncoins must be between 10000000 and 10000000000000.
     */
    public val amount: Int,
)
