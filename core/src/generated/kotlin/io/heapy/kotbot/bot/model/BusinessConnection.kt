package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes the connection of the bot with a business account.
 */
@Serializable
public data class BusinessConnection(
    /**
     * Unique identifier of the business connection
     */
    public val id: String,
    /**
     * Business account user that created the business connection
     */
    public val user: User,
    /**
     * Identifier of a private chat with the user who created the business connection. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val user_chat_id: Long,
    /**
     * Date the connection was established in Unix time
     */
    public val date: Long,
    /**
     * *Optional*. Rights of the business bot
     */
    public val rights: BusinessBotRights? = null,
    /**
     * *True*, if the connection is active
     */
    public val is_enabled: Boolean,
)
