package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes the opening hours of a business.
 */
@Serializable
public data class BusinessOpeningHours(
    /**
     * Unique name of the time zone for which the opening hours are defined
     */
    public val time_zone_name: String,
    /**
     * List of time intervals describing business opening hours
     */
    public val opening_hours: List<BusinessOpeningHoursInterval>,
)
