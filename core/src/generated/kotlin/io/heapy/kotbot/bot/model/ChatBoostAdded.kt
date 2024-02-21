package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user boosting a chat.
 */
@Serializable
public data class ChatBoostAdded(
    /**
     * Number of boosts added by the user
     */
    public val boost_count: Int,
)
