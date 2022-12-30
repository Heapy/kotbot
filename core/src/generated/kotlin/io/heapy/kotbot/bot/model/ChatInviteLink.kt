package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an invite link for a chat.
 */
@Serializable
public data class ChatInviteLink(
    /**
     * The invite link. If the link was created by another chat administrator, then the second part of the link will be replaced with "…".
     */
    public val invite_link: String,
    /**
     * Creator of the link
     */
    public val creator: User,
    /**
     * *True*, if users joining the chat via the link need to be approved by chat administrators
     */
    public val creates_join_request: Boolean,
    /**
     * *True*, if the link is primary
     */
    public val is_primary: Boolean,
    /**
     * *True*, if the link is revoked
     */
    public val is_revoked: Boolean,
    /**
     * *Optional*. Invite link name
     */
    public val name: String? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the link will expire or has been expired
     */
    public val expire_date: Long? = null,
    /**
     * *Optional*. The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
     */
    public val member_limit: Int? = null,
    /**
     * *Optional*. Number of pending join requests created using this link
     */
    public val pending_join_request_count: Int? = null,
)
