package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a change in the price of direct messages sent to a channel chat.
 */
@Serializable
public data class DirectMessagePriceChanged(
    /**
     * *True*, if direct messages are enabled for the channel chat; false otherwise
     */
    public val are_direct_messages_enabled: Boolean,
    /**
     * *Optional*. The new number of Telegram Stars that must be paid by users for each direct message sent to the channel. Does not apply to users who have been exempted by administrators. Defaults to 0.
     */
    public val direct_message_star_count: Int? = null,
)
