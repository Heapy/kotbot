package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to another user.
 */
@Serializable
public data class ChatBoostSourcePremium(
    /**
     * Source of the boost, always "premium"
     */
    public val source: String,
    /**
     * User that boosted the chat
     */
    public val user: User,
) : ChatBoostSource
