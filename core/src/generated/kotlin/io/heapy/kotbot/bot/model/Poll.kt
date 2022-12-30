package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about a poll.
 */
@Serializable
public data class Poll(
    /**
     * Unique poll identifier
     */
    public val id: String,
    /**
     * Poll question, 1-300 characters
     */
    public val question: String,
    /**
     * List of poll options
     */
    public val options: List<PollOption>,
    /**
     * Total number of users that voted in the poll
     */
    public val total_voter_count: Int,
    /**
     * *True*, if the poll is closed
     */
    public val is_closed: Boolean,
    /**
     * *True*, if the poll is anonymous
     */
    public val is_anonymous: Boolean,
    /**
     * Poll type, currently can be "regular" or "quiz"
     */
    public val type: String,
    /**
     * *True*, if the poll allows multiple answers
     */
    public val allows_multiple_answers: Boolean,
    /**
     * *Optional*. 0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are closed, or was sent (not forwarded) by the bot or to the private chat with the bot.
     */
    public val correct_option_id: Int? = null,
    /**
     * *Optional*. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters
     */
    public val explanation: String? = null,
    /**
     * *Optional*. Special entities like usernames, URLs, bot commands, etc. that appear in the *explanation*
     */
    public val explanation_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Amount of time in seconds the poll will be active after creation
     */
    public val open_period: Int? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the poll will be automatically closed
     */
    public val close_date: Long? = null,
)
