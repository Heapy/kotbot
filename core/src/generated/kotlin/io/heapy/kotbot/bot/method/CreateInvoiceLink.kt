package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.LabeledPrice
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to create a link for an invoice. Returns the created invoice link as *String* on success.
 */
@Serializable
public data class CreateInvoiceLink(
    /**
     * Unique identifier of the business connection on behalf of which the link will be created. For payments in [Telegram Stars](https://t.me/BotNews/90) only.
     */
    public val business_connection_id: String? = null,
    /**
     * Product name, 1-32 characters
     */
    public val title: String,
    /**
     * Product description, 1-255 characters
     */
    public val description: String,
    /**
     * Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal processes.
     */
    public val payload: String,
    /**
     * Payment provider token, obtained via [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val provider_token: String? = null,
    /**
     * Three-letter ISO 4217 currency code, see [more on currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass "XTR" for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val currency: String,
    /**
     * Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val prices: List<LabeledPrice>,
    /**
     * The number of seconds the subscription will be active for before the next payment. The currency must be set to "XTR" (Telegram Stars) if the parameter is used. Currently, it must always be 2592000 (30 days) if specified. Any number of subscriptions can be active for a given bot at the same time, including multiple concurrent subscriptions from the same user. Subscription price must no exceed 2500 Telegram Stars.
     */
    public val subscription_period: Int? = null,
    /**
     * The maximum accepted amount for tips in the *smallest units* of the currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val max_tip_amount: Int? = null,
    /**
     * A JSON-serialized array of suggested amounts of tips in the *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed *max_tip_amount*.
     */
    public val suggested_tip_amounts: List<Int>? = null,
    /**
     * JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
     */
    public val provider_data: String? = null,
    /**
     * URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
     */
    public val photo_url: String? = null,
    /**
     * Photo size in bytes
     */
    public val photo_size: Int? = null,
    /**
     * Photo width
     */
    public val photo_width: Int? = null,
    /**
     * Photo height
     */
    public val photo_height: Int? = null,
    /**
     * Pass *True* if you require the user's full name to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val need_name: Boolean? = null,
    /**
     * Pass *True* if you require the user's phone number to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val need_phone_number: Boolean? = null,
    /**
     * Pass *True* if you require the user's email address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val need_email: Boolean? = null,
    /**
     * Pass *True* if you require the user's shipping address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val need_shipping_address: Boolean? = null,
    /**
     * Pass *True* if the user's phone number should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val send_phone_number_to_provider: Boolean? = null,
    /**
     * Pass *True* if the user's email address should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val send_email_to_provider: Boolean? = null,
    /**
     * Pass *True* if the final price depends on the shipping method. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val is_flexible: Boolean? = null,
) : Method<CreateInvoiceLink, String> by Companion {
    public companion object : Method<CreateInvoiceLink, String> {
        override val _deserializer: KSerializer<Response<String>> =
                Response.serializer(String.serializer())

        override val _serializer: KSerializer<CreateInvoiceLink> = serializer()

        override val _name: String = "createInvoiceLink"
    }
}
