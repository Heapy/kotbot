package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a guest bot.
 */
@Serializable
public data class SentGuestMessage(
    /**
     * Identifier of the sent inline message
     */
    public val inline_message_id: String,
)
