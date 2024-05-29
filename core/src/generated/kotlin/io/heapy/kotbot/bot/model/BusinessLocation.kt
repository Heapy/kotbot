package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Contains information about the location of a Telegram Business account.
 */
@Serializable
public data class BusinessLocation(
    /**
     * Address of the business
     */
    public val address: String,
    /**
     * *Optional*. Location of the business
     */
    public val location: Location? = null,
)
