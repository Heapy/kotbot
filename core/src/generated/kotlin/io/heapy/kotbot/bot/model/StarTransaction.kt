package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a Telegram Star transaction.
 */
@Serializable
public data class StarTransaction(
    /**
     * Unique identifier of the transaction. Coincides with the identifier of the original transaction for refund transactions. Coincides with *SuccessfulPayment.telegram_payment_charge_id* for successful incoming payments from users.
     */
    public val id: String,
    /**
     * Number of Telegram Stars transferred by the transaction
     */
    public val amount: Int,
    /**
     * Date the transaction was created in Unix time
     */
    public val date: Long,
    /**
     * *Optional*. Source of an incoming transaction (e.g., a user purchasing goods or services, Fragment refunding a failed withdrawal). Only for incoming transactions
     */
    public val source: TransactionPartner? = null,
    /**
     * *Optional*. Receiver of an outgoing transaction (e.g., a user for a purchase refund, Fragment for a withdrawal). Only for outgoing transactions
     */
    public val `receiver`: TransactionPartner? = null,
)
