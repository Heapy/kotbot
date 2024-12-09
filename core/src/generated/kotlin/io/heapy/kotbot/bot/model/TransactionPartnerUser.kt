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
     * Information about the user
     */
    public val user: User,
    /**
     * *Optional*. Information about the affiliate that received a commission via this transaction
     */
    public val affiliate: AffiliateInfo? = null,
    /**
     * *Optional*. Bot-specified invoice payload
     */
    public val invoice_payload: String? = null,
    /**
     * *Optional*. The duration of the paid subscription
     */
    public val subscription_period: Int? = null,
    /**
     * *Optional*. Information about the paid media bought by the user
     */
    public val paid_media: List<PaidMedia>? = null,
    /**
     * *Optional*. Bot-specified paid media payload
     */
    public val paid_media_payload: String? = null,
    /**
     * *Optional*. The gift sent to the user by the bot
     */
    public val gift: Gift? = null,
) : TransactionPartner
