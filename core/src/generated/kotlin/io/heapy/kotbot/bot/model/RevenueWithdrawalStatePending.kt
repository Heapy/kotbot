package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The withdrawal is in progress.
 */
@Serializable
public data class RevenueWithdrawalStatePending(
    /**
     * Type of the state, always "pending"
     */
    public val type: String,
) : RevenueWithdrawalState
