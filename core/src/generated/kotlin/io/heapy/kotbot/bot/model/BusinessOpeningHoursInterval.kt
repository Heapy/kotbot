package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Describes an interval of time during which a business is open.
 */
@Serializable
public data class BusinessOpeningHoursInterval(
    /**
     * The minute's sequence number in a week, starting on Monday, marking the start of the time interval during which the business is open; 0 - 7 \* 24 \* 60
     */
    public val opening_minute: Int,
    /**
     * The minute's sequence number in a week, starting on Monday, marking the end of the time interval during which the business is open; 0 - 8 \* 24 \* 60
     */
    public val closing_minute: Int,
)
