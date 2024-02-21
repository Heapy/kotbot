package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents a story.
 */
@Serializable
public data class Story(
    /**
     * Chat that posted the story
     */
    public val chat: Chat,
    /**
     * Unique identifier for the story in the chat
     */
    public val id: Int,
)
