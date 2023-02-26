package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a join request sent to a chat.
 */
@Serializable
public data class ChatJoinRequest(
    /**
     * Chat to which the request was sent
     */
    public val chat: Chat,
    /**
     * User that sent the join request
     */
    public val from: User,
    /**
     * Identifier of a private chat with the user who sent the join request. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot can use this identifier for 24 hours to send messages until the join request is processed, assuming no other administrator contacted the user.
     */
    public val user_chat_id: Long,
    /**
     * Date the request was sent in Unix time
     */
    public val date: Long,
    /**
     * *Optional*. Bio of the user.
     */
    public val bio: String? = null,
    /**
     * *Optional*. Chat invite link that was used by the user to send the join request
     */
    public val invite_link: ChatInviteLink? = null,
)
