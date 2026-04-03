package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an option deleted from a poll.
 */
@Serializable
public data class PollOptionDeleted(
    /**
     * *Optional*. Message containing the poll from which the option was deleted, if known. Note that the [Message](https://core.telegram.org/bots/api/#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    public val poll_message: MaybeInaccessibleMessage? = null,
    /**
     * Unique identifier of the deleted option
     */
    public val option_persistent_id: String,
    /**
     * Option text
     */
    public val option_text: String,
    /**
     * *Optional*. Special entities that appear in the *option_text*
     */
    public val option_text_entities: List<MessageEntity>? = null,
)
