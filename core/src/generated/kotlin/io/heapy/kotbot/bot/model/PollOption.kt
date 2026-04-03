package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 */
@Serializable
public data class PollOption(
    /**
     * Unique identifier of the option, persistent on option addition and deletion
     */
    public val persistent_id: String,
    /**
     * Option text, 1-100 characters
     */
    public val text: String,
    /**
     * *Optional*. Special entities that appear in the option *text*. Currently, only custom emoji entities are allowed in poll option texts
     */
    public val text_entities: List<MessageEntity>? = null,
    /**
     * Number of users who voted for this option; may be 0 if unknown
     */
    public val voter_count: Int,
    /**
     * *Optional*. User who added the option; omitted if the option wasn't added by a user after poll creation
     */
    public val added_by_user: User? = null,
    /**
     * *Optional*. Chat that added the option; omitted if the option wasn't added by a chat after poll creation
     */
    public val added_by_chat: Chat? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the option was added; omitted if the option existed in the original poll
     */
    public val addition_date: Long? = null,
)
