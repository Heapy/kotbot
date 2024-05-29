package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 */
@Serializable
public data class PollOption(
    /**
     * Option text, 1-100 characters
     */
    public val text: String,
    /**
     * *Optional*. Special entities that appear in the option *text*. Currently, only custom emoji entities are allowed in poll option texts
     */
    public val text_entities: List<MessageEntity>? = null,
    /**
     * Number of users that voted for this option
     */
    public val voter_count: Int,
)
