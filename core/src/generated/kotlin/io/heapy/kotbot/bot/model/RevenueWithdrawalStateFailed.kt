package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The withdrawal failed and the transaction was refunded.
 */
@Serializable
public data class RevenueWithdrawalStateFailed(
    /**
     * Type of the state, always "failed"
     */
    public val type: String,
) : RevenueWithdrawalState
