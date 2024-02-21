package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object describes a message that was deleted or is otherwise inaccessible to the bot.
 */
@Serializable
public data class InaccessibleMessage(
    /**
     * Chat the message belonged to
     */
    public val chat: Chat,
    /**
     * Unique message identifier inside the chat
     */
    public val message_id: Int,
    /**
     * Always 0. The field can be used to differentiate regular and inaccessible messages.
     */
    public val date: Int,
) : MaybeInaccessibleMessage
