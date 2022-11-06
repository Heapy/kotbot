package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a forum topic.
 */
@Serializable
public data class ForumTopic(
    /**
     * Unique identifier of the forum topic
     */
    public val message_thread_id: Int,
    /**
     * Name of the topic
     */
    public val name: String,
    /**
     * Color of the topic icon in RGB format
     */
    public val icon_color: Int,
    /**
     * *Optional*. Unique identifier of the custom emoji shown as the topic icon
     */
    public val icon_custom_emoji_id: String? = null,
)
