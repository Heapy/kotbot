package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The message was originally sent on behalf of a chat to a group chat.
 */
@Serializable
public data class MessageOriginChat(
    /**
     * Type of the message origin, always "chat"
     */
    public val type: String = "chat",
    /**
     * Date the message was sent originally in Unix time
     */
    public val date: Long,
    /**
     * Chat that sent the message originally
     */
    public val sender_chat: Chat,
    /**
     * *Optional*. For messages originally sent by an anonymous chat administrator, original message author signature
     */
    public val author_signature: String? = null,
) : MessageOrigin
