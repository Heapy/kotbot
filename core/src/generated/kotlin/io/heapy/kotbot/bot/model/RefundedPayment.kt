package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about a refunded payment.
 */
@Serializable
public data class RefundedPayment(
    /**
     * Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code, or "XTR" for payments in [Telegram Stars](https://t.me/BotNews/90). Currently, always "XTR"
     */
    public val currency: String,
    /**
     * Total refunded price in the *smallest units* of the currency (integer, **not** float/double). For example, for a price of `US$ 1.45`, `total_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     */
    public val total_amount: Int,
    /**
     * Bot-specified invoice payload
     */
    public val invoice_payload: String,
    /**
     * Telegram payment identifier
     */
    public val telegram_payment_charge_id: String,
    /**
     * *Optional*. Provider payment identifier
     */
    public val provider_payment_charge_id: String? = null,
)
