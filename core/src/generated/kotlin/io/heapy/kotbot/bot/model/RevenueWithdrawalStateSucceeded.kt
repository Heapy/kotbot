package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The withdrawal succeeded.
 */
@Serializable
public data class RevenueWithdrawalStateSucceeded(
    /**
     * Type of the state, always "succeeded"
     */
    public val type: String,
    /**
     * Date the withdrawal was completed in Unix time
     */
    public val date: Long,
    /**
     * An HTTPS URL that can be used to see transaction details
     */
    public val url: String,
) : RevenueWithdrawalState
