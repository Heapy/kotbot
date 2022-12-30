package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about an edited forum topic.
 */
@Serializable
public data class ForumTopicEdited(
    /**
     * *Optional*. New name of the topic, if it was edited
     */
    public val name: String? = null,
    /**
     * *Optional*. New identifier of the custom emoji shown as the topic icon, if it was edited; an empty string if the icon was removed
     */
    public val icon_custom_emoji_id: String? = null,
)
