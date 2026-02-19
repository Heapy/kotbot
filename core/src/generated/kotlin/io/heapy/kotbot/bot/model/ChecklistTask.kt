package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a task in a checklist.
 */
@Serializable
public data class ChecklistTask(
    /**
     * Unique identifier of the task
     */
    public val id: Int,
    /**
     * Text of the task
     */
    public val text: String,
    /**
     * *Optional*. Special entities that appear in the task text
     */
    public val text_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. User that completed the task; omitted if the task wasn't completed by a user
     */
    public val completed_by_user: User? = null,
    /**
     * *Optional*. Chat that completed the task; omitted if the task wasn't completed by a chat
     */
    public val completed_by_chat: Chat? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the task was completed; 0 if the task wasn't completed
     */
    public val completion_date: Long? = null,
)
