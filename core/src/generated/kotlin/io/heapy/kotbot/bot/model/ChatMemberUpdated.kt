package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object represents changes in the status of a chat member.
 */
@Serializable
public data class ChatMemberUpdated(
    /**
     * Chat the user belongs to
     */
    public val chat: Chat,
    /**
     * Performer of the action, which resulted in the change
     */
    public val from: User,
    /**
     * Date the change was done in Unix time
     */
    public val date: Long,
    /**
     * Previous information about the chat member
     */
    public val old_chat_member: ChatMember,
    /**
     * New information about the chat member
     */
    public val new_chat_member: ChatMember,
    /**
     * *Optional*. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
     */
    public val invite_link: ChatInviteLink? = null,
    /**
     * *Optional*. *True*, if the user joined the chat after sending a direct join request without using an invite link and being approved by an administrator
     */
    public val via_join_request: Boolean? = null,
    /**
     * *Optional*. *True*, if the user joined the chat via a chat folder invite link
     */
    public val via_chat_folder_invite_link: Boolean? = null,
)
