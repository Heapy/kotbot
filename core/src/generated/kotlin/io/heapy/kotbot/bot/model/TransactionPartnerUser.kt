package io.heapy.kotbot.bot.model

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
     * *Optional*. Bot-specified invoice payload
     */
    public val invoice_payload: String? = null,
    /**
     * *Optional*. Information about the paid media bought by the user
     */
    public val paid_media: List<PaidMedia>? = null,
) : TransactionPartner
