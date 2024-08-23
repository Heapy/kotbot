package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object contains information about an incoming pre-checkout query.
 */
@Serializable
public data class PreCheckoutQuery(
    /**
     * Unique query identifier
     */
    public val id: String,
    /**
     * User who sent the query
     */
    public val from: User,
    /**
     * Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code, or "XTR" for payments in [Telegram Stars](https://t.me/BotNews/90)
     */
    public val currency: String,
    /**
     * Total price in the *smallest units* of the currency (integer, **not** float/double). For example, for a price of `US$ 1.45` pass `amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     */
    public val total_amount: Int,
    /**
     * Bot-specified invoice payload
     */
    public val invoice_payload: String,
    /**
     * *Optional*. Identifier of the shipping option chosen by the user
     */
    public val shipping_option_id: String? = null,
    /**
     * *Optional*. Order information provided by the user
     */
    public val order_info: OrderInfo? = null,
)
