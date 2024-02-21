package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object contains information about a chat boost.
 */
@Serializable
public data class ChatBoost(
    /**
     * Unique identifier of the boost
     */
    public val boost_id: String,
    /**
     * Point in time (Unix timestamp) when the chat was boosted
     */
    public val add_date: Long,
    /**
     * Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged
     */
    public val expiration_date: Long,
    /**
     * Source of the added boost
     */
    public val source: ChatBoostSource,
)
