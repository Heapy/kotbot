package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a shipping address.
 */
@Serializable
public data class ShippingAddress(
    /**
     * Two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code
     */
    public val country_code: String,
    /**
     * State, if applicable
     */
    public val state: String,
    /**
     * City
     */
    public val city: String,
    /**
     * First line for the address
     */
    public val street_line1: String,
    /**
     * Second line for the address
     */
    public val street_line2: String,
    /**
     * Address post code
     */
    public val post_code: String,
)
