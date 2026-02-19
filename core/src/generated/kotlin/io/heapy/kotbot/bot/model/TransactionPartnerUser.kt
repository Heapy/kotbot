package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a transaction with a user.
 */
@Serializable
public data class TransactionPartnerUser(
    /**
     * Type of the transaction partner, always "user"
     */
    public val type: String,
    /**
     * Type of the transaction, currently one of "invoice_payment" for payments via invoices, "paid_media_payment" for payments for paid media, "gift_purchase" for gifts sent by the bot, "premium_purchase" for Telegram Premium subscriptions gifted by the bot, "business_account_transfer" for direct transfers from managed business accounts
     */
    public val transaction_type: String,
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *Optional*. Information about the affiliate that received a commission via this transaction. Can be available only for "invoice_payment" and "paid_media_payment" transactions.
     */
    public val affiliate: AffiliateInfo? = null,
    /**
     * *Optional*. Bot-specified invoice payload. Can be available only for "invoice_payment" transactions.
     */
    public val invoice_payload: String? = null,
    /**
     * *Optional*. The duration of the paid subscription. Can be available only for "invoice_payment" transactions.
     */
    public val subscription_period: Int? = null,
    /**
     * *Optional*. Information about the paid media bought by the user; for "paid_media_payment" transactions only
     */
    public val paid_media: List<PaidMedia>? = null,
    /**
     * *Optional*. Bot-specified paid media payload. Can be available only for "paid_media_payment" transactions.
     */
    public val paid_media_payload: String? = null,
    /**
     * *Optional*. The gift sent to the user by the bot; for "gift_purchase" transactions only
     */
    public val gift: Gift? = null,
    /**
     * *Optional*. Number of months the gifted Telegram Premium subscription will be active for; for "premium_purchase" transactions only
     */
    public val premium_subscription_duration: Int? = null,
) : TransactionPartner
