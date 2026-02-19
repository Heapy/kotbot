package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a checklist.
 */
@Serializable
public data class Checklist(
    /**
     * Title of the checklist
     */
    public val title: String,
    /**
     * *Optional*. Special entities that appear in the checklist title
     */
    public val title_entities: List<MessageEntity>? = null,
    /**
     * List of tasks in the checklist
     */
    public val tasks: List<ChecklistTask>,
    /**
     * *Optional*. *True*, if users other than the creator of the list can add tasks to the list
     */
    public val others_can_add_tasks: Boolean? = null,
    /**
     * *Optional*. *True*, if users other than the creator of the list can mark tasks as done or not done
     */
    public val others_can_mark_tasks_as_done: Boolean? = null,
)
