package io.heapy.kotbot.bot.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a service message about tasks added to a checklist.
 */
@Serializable
public data class ChecklistTasksAdded(
    /**
     * *Optional*. Message containing the checklist to which the tasks were added. Note that the [Message](https://core.telegram.org/bots/api/#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    public val checklist_message: Message? = null,
    /**
     * List of tasks added to the checklist
     */
    public val tasks: List<ChecklistTask>,
)
