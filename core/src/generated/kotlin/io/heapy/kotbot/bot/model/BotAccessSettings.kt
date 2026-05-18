package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object describes the access settings of a bot.
 */
@Serializable
public data class BotAccessSettings(
    /**
     * *True*, if only selected users can access the bot. The bot's owner can always access it.
     */
    public val is_access_restricted: Boolean,
    /**
     * *Optional*. The list of other users who have access to the bot if the access is restricted
     */
    public val added_users: List<User>? = null,
)
