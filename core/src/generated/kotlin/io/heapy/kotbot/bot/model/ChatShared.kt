package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object contains information about the chat whose identifier was shared with the bot using a [KeyboardButtonRequestChat](https://core.telegram.org/bots/api/#keyboardbuttonrequestchat) button.
 */
@Serializable
public data class ChatShared(
    /**
     * Identifier of the request
     */
    public val request_id: Int,
    /**
     * Identifier of the shared chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the chat and could be unable to use this identifier, unless the chat is already known to the bot by some other means.
     */
    public val chat_id: Long,
)
