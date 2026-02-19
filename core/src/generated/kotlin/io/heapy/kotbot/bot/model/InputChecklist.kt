package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a checklist to create.
 */
@Serializable
public data class InputChecklist(
    /**
     * Title of the checklist; 1-255 characters after entities parsing
     */
    public val title: String,
    /**
     * *Optional*. Mode for parsing entities in the title. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the title, which can be specified instead of parse_mode. Currently, only *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, and *custom_emoji* entities are allowed.
     */
    public val title_entities: List<MessageEntity>? = null,
    /**
     * List of 1-30 tasks in the checklist
     */
    public val tasks: List<InputChecklistTask>,
    /**
     * *Optional*. Pass *True* if other users can add tasks to the checklist
     */
    public val others_can_add_tasks: Boolean? = null,
    /**
     * *Optional*. Pass *True* if other users can mark tasks as done or not done in the checklist
     */
    public val others_can_mark_tasks_as_done: Boolean? = null,
)
