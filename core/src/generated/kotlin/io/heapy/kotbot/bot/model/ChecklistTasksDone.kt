package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a service message about checklist tasks marked as done or not done.
 */
@Serializable
public data class ChecklistTasksDone(
    /**
     * *Optional*. Message containing the checklist whose tasks were marked as done or not done. Note that the [Message](https://core.telegram.org/bots/api/#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    public val checklist_message: Message? = null,
    /**
     * *Optional*. Identifiers of the tasks that were marked as done
     */
    public val marked_as_done_task_ids: List<Int>? = null,
    /**
     * *Optional*. Identifiers of the tasks that were marked as not done
     */
    public val marked_as_not_done_task_ids: List<Int>? = null,
)
