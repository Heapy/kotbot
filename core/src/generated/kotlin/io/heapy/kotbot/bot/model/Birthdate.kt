package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Describes the birthdate of a user.
 */
@Serializable
public data class Birthdate(
    /**
     * Day of the user's birth; 1-31
     */
    public val day: Int,
    /**
     * Month of the user's birth; 1-12
     */
    public val month: Int,
    /**
     * *Optional*. Year of the user's birth
     */
    public val year: Int? = null,
)
