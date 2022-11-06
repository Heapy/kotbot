package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a new forum topic created in the chat.
 */
@Serializable
public data class ForumTopicCreated(
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
