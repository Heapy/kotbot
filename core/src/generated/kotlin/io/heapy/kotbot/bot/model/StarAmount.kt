package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Describes an amount of Telegram Stars.
 */
@Serializable
public data class StarAmount(
    /**
     * Integer amount of Telegram Stars, rounded to 0; can be negative
     */
    public val amount: Int,
    /**
     * *Optional*. The number of 1/1000000000 shares of Telegram Stars; from -999999999 to 999999999; can be negative if and only if *amount* is non-positive
     */
    public val nanostar_amount: Int? = null,
)
