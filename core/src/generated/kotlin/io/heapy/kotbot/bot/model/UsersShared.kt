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
     * Identifiers of the shared users. These numbers may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting them. But they have at most 52 significant bits, so 64-bit integers or double-precision float types are safe for storing these identifiers. The bot may not have access to the users and could be unable to use these identifiers, unless the users are already known to the bot by some other means.
     */
    public val user_ids: List<Int>,
)
