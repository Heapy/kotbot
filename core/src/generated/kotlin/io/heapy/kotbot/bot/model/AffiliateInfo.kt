package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Contains information about the affiliate that received a commission via this transaction.
 */
@Serializable
public data class AffiliateInfo(
    /**
     * *Optional*. The bot or the user that received an affiliate commission if it was received by a bot or a user
     */
    public val affiliate_user: User? = null,
    /**
     * *Optional*. The chat that received an affiliate commission if it was received by a chat
     */
    public val affiliate_chat: Chat? = null,
    /**
     * The number of Telegram Stars received by the affiliate for each 1000 Telegram Stars received by the bot from referred users
     */
    public val commission_per_mille: Int,
    /**
     * Integer amount of Telegram Stars received by the affiliate from the transaction, rounded to 0; can be negative for refunds
     */
    public val amount: Int,
    /**
     * *Optional*. The number of 1/1000000000 shares of Telegram Stars received by the affiliate; from -999999999 to 999999999; can be negative for refunds
     */
    public val nanostar_amount: Int? = null,
)
