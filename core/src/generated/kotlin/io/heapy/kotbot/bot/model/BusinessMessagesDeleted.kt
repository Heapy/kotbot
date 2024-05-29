package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object is received when messages are deleted from a connected business account.
 */
@Serializable
public data class BusinessMessagesDeleted(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Information about a chat in the business account. The bot may not have access to the chat or the corresponding user.
     */
    public val chat: Chat,
    /**
     * The list of identifiers of deleted messages in the chat of the business account
     */
    public val message_ids: List<Int>,
)
