package io.heapy.kotbot.bot.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about new members invited to a video chat.
 */
@Serializable
public data class VideoChatParticipantsInvited(
    /**
     * New members that were invited to the video chat
     */
    public val users: List<User>,
)
