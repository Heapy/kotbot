package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The message was originally sent by an unknown user.
 */
@Serializable
public data class MessageOriginHiddenUser(
    /**
     * Type of the message origin, always "hidden_user"
     */
    public val type: String = "hidden_user",
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * Name of the user that sent the message originally
     */
    public val sender_user_name: String,
) : MessageOrigin
