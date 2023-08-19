package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents an answer of a user in a non-anonymous poll.
 */
@Serializable
public data class PollAnswer(
    /**
     * Unique poll identifier
     */
    public val poll_id: String,
    /**
     * *Optional*. The chat that changed the answer to the poll, if the voter is anonymous
     */
    public val voter_chat: Chat? = null,
    /**
     * *Optional*. The user that changed the answer to the poll, if the voter isn't anonymous
     */
    public val user: User? = null,
    /**
     * 0-based identifiers of chosen answer options. May be empty if the vote was retracted.
     */
    public val option_ids: List<Int>,
)
