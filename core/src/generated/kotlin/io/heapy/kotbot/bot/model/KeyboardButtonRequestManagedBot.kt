package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object defines the parameters for the creation of a managed bot. Information about the created bot will be shared with the bot using the update *managed_bot* and a [Message](https://core.telegram.org/bots/api/#message) with the field *managed_bot_created*.
 */
@Serializable
public data class KeyboardButtonRequestManagedBot(
    /**
     * Signed 32-bit identifier of the request. Must be unique within the message
     */
    public val request_id: Int,
    /**
     * *Optional*. Suggested name for the bot
     */
    public val suggested_name: String? = null,
    /**
     * *Optional*. Suggested username for the bot
     */
    public val suggested_username: String? = null,
)
