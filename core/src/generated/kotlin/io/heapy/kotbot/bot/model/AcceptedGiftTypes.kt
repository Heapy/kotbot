package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * This object describes the types of gifts that can be gifted to a user or a chat.
 */
@Serializable
public data class AcceptedGiftTypes(
    /**
     * *True*, if unlimited regular gifts are accepted
     */
    public val unlimited_gifts: Boolean,
    /**
     * *True*, if limited regular gifts are accepted
     */
    public val limited_gifts: Boolean,
    /**
     * *True*, if unique gifts or gifts that can be upgraded to unique for free are accepted
     */
    public val unique_gifts: Boolean,
    /**
     * *True*, if a Telegram Premium subscription is accepted
     */
    public val premium_subscription: Boolean,
    /**
     * *True*, if transfers of unique gifts from channels are accepted
     */
    public val gifts_from_channels: Boolean,
)
