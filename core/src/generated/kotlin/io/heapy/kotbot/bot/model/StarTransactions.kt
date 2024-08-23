package io.heapy.kotbot.bot.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Contains a list of Telegram Star transactions.
 */
@Serializable
public data class StarTransactions(
    /**
     * The list of transactions
     */
    public val transactions: List<StarTransaction>,
)
