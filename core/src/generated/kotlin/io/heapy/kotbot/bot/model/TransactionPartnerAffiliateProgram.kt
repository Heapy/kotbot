package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes the affiliate program that issued the affiliate commission received via this transaction.
 */
@Serializable
public data class TransactionPartnerAffiliateProgram(
    /**
     * Type of the transaction partner, always "affiliate_program"
     */
    public val type: String,
    /**
     * *Optional*. Information about the bot that sponsored the affiliate program
     */
    public val sponsor_user: User? = null,
    /**
     * The number of Telegram Stars received by the bot for each 1000 Telegram Stars received by the affiliate program sponsor from referred users
     */
    public val commission_per_mille: Int,
) : TransactionPartner
