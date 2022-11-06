package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents information about an order.
 */
@Serializable
public data class OrderInfo(
    /**
     * *Optional*. User name
     */
    public val name: String? = null,
    /**
     * *Optional*. User's phone number
     */
    public val phone_number: String? = null,
    /**
     * *Optional*. User email
     */
    public val email: String? = null,
    /**
     * *Optional*. User shipping address
     */
    public val shipping_address: ShippingAddress? = null,
)
