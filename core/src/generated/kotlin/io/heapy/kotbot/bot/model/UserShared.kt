package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object contains information about the user whose identifier was shared with the bot using a [KeyboardButtonRequestUser](https://core.telegram.org/bots/api/#keyboardbuttonrequestuser) button.
 */
@Serializable
public data class UserShared(
    /**
     * Identifier of the request
     */
    public val request_id: Int,
    /**
     * Identifier of the shared user. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the user and could be unable to use this identifier, unless the user is already known to the bot by some other means.
     */
    public val user_id: Long,
)
