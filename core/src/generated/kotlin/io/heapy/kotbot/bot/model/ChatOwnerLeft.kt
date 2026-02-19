package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * Describes a service message about the chat owner leaving the chat.
 */
@Serializable
public data class ChatOwnerLeft(
    /**
     * *Optional*. The user which will be the new owner of the chat if the previous owner does not return to the chat
     */
    public val new_owner: User? = null,
)
