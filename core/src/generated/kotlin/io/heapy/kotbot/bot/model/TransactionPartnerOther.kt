package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a transaction with an unknown source or recipient.
 */
@Serializable
public data class TransactionPartnerOther(
    /**
     * Type of the transaction partner, always "other"
     */
    public val type: String,
) : TransactionPartner
