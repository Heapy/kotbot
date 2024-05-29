package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about the users whose identifiers were shared with the bot using a [KeyboardButtonRequestUsers](https://core.telegram.org/bots/api/#keyboardbuttonrequestusers) button.
 */
@Serializable
public data class UsersShared(
    /**
     * Identifier of the request
     */
    public val request_id: Int,
    /**
     * Information about users shared with the bot.
     */
    public val users: List<SharedUser>,
)
