package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a unique message identifier.
 */
@Serializable
public data class MessageId(
    /**
     * Unique message identifier
     */
    public val message_id: Int,
)
