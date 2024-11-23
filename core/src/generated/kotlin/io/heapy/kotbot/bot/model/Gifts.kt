package io.heapy.kotbot.bot.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represent a list of gifts.
 */
@Serializable
public data class Gifts(
    /**
     * The list of gifts
     */
    public val gifts: List<Gift>,
)
