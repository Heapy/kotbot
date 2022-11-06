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
