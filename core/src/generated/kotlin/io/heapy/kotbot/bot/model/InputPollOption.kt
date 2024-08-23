package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll to be sent.
 */
@Serializable
public data class InputPollOption(
    /**
     * Option text, 1-100 characters
     */
    public val text: String,
    /**
     * *Optional*. Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details. Currently, only custom emoji entities are allowed
     */
    public val text_parse_mode: String? = null,
    /**
     * *Optional*. A JSON-serialized list of special entities that appear in the poll option text. It can be specified instead of *text_parse_mode*
     */
    public val text_entities: List<MessageEntity>? = null,
)
