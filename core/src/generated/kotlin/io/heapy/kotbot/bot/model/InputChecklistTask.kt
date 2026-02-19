package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a task to add to a checklist.
 */
@Serializable
public data class InputChecklistTask(
    /**
     * Unique identifier of the task; must be positive and unique among all task identifiers currently present in the checklist
     */
    public val id: Int,
    /**
     * Text of the task; 1-100 characters after entities parsing
     */
    public val text: String,
    /**
     * *Optional*. Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the text, which can be specified instead of parse_mode. Currently, only *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, and *custom_emoji* entities are allowed.
     */
    public val text_entities: List<MessageEntity>? = null,
)
