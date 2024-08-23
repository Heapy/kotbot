package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object contains information about an incoming shipping query.
 */
@Serializable
public data class ShippingQuery(
    /**
     * Unique query identifier
     */
    public val id: String,
    /**
     * User who sent the query
     */
    public val from: User,
    /**
     * Bot-specified invoice payload
     */
    public val invoice_payload: String,
    /**
     * User specified shipping address
     */
    public val shipping_address: ShippingAddress,
)
