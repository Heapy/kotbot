package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes an inline message to be sent by a user of a Mini App.
 */
@Serializable
public data class PreparedInlineMessage(
    /**
     * Unique identifier of the prepared message
     */
    public val id: String,
    /**
     * Expiration date of the prepared message, in Unix time. Expired prepared messages can no longer be used
     */
    public val expiration_date: Long,
)
