package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a video chat scheduled in the chat.
 */
@Serializable
public data class VideoChatScheduled(
    /**
     * Point in time (Unix timestamp) when the video chat is supposed to be started by a chat administrator
     */
    public val start_date: Long,
)
