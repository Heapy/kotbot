package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * Describes a service message about an ownership change in the chat.
 */
@Serializable
public data class ChatOwnerChanged(
    /**
     * The new owner of the chat
     */
    public val new_owner: User,
)
