package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object describes the rating of a user based on their Telegram Star spendings.
 */
@Serializable
public data class UserRating(
    /**
     * Current level of the user, indicating their reliability when purchasing digital goods and services. A higher level suggests a more trustworthy customer; a negative level is likely reason for concern.
     */
    public val level: Int,
    /**
     * Numerical value of the user's rating; the higher the rating, the better
     */
    public val rating: Int,
    /**
     * The rating value required to get the current level
     */
    public val current_level_rating: Int,
    /**
     * *Optional*. The rating value required to get to the next level; omitted if the maximum level was reached
     */
    public val next_level_rating: Int? = null,
)
