package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a transaction with payment for [paid broadcasting](https://core.telegram.org/bots/api/#paid-broadcasts).
 */
@Serializable
public data class TransactionPartnerTelegramApi(
    /**
     * Type of the transaction partner, always "telegram_api"
     */
    public val type: String,
    /**
     * The number of successful requests that exceeded regular limits and were therefore billed
     */
    public val request_count: Int,
) : TransactionPartner
