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
     * The user, who changed the answer to the poll
     */
    public val user: User,
    /**
     * 0-based identifiers of answer options, chosen by the user. May be empty if the user retracted their vote.
     */
    public val option_ids: List<Int>,
)
