package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a withdrawal transaction with Fragment.
 */
@Serializable
public data class TransactionPartnerFragment(
    /**
     * Type of the transaction partner, always "fragment"
     */
    public val type: String,
    /**
     * *Optional*. State of the transaction if the transaction is outgoing
     */
    public val withdrawal_state: RevenueWithdrawalState? = null,
) : TransactionPartner
