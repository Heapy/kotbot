package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The message was originally sent by a known user.
 */
@Serializable
public data class MessageOriginUser(
    /**
     * Type of the message origin, always "user"
     */
    public val type: String,
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * User that sent the message originally
     */
    public val sender_user: User,
) : MessageOrigin
