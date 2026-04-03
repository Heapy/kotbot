package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an option added to a poll.
 */
@Serializable
public data class PollOptionAdded(
    /**
     * *Optional*. Message containing the poll to which the option was added, if known. Note that the [Message](https://core.telegram.org/bots/api/#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    public val poll_message: MaybeInaccessibleMessage? = null,
    /**
     * Unique identifier of the added option
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
