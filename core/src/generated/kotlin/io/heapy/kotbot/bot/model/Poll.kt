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
     * *Optional*. Special entities that appear in the *question*. Currently, only custom emoji entities are allowed in poll questions
     */
    public val question_entities: List<MessageEntity>? = null,
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
     * *True*, if the poll allows to change the chosen answer options
     */
    public val allows_revoting: Boolean,
    /**
     * *True* if voting is limited to users who have been members of the chat where the poll was originally sent for more than 24 hours
     */
    public val members_only: Boolean,
    /**
     * *Optional*. A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes indicating the countries from which users can vote in the poll. The country code "FT" is used for users with anonymous numbers. If omitted, then users from any country can participate in the poll.
     */
    public val country_codes: List<String>? = null,
    /**
     * *Optional*. Array of 0-based identifiers of the correct answer options. Available only for polls in quiz mode which are closed or were sent (not forwarded) by the bot or to the private chat with the bot.
     */
    public val correct_option_ids: List<Int>? = null,
    /**
     * *Optional*. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters
     */
    public val explanation: String? = null,
    /**
     * *Optional*. Special entities like usernames, URLs, bot commands, etc. that appear in the *explanation*
     */
    public val explanation_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Media added to the quiz explanation
     */
    public val explanation_media: PollMedia? = null,
    /**
     * *Optional*. Amount of time in seconds the poll will be active after creation
     */
    public val open_period: Int? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the poll will be automatically closed
     */
    public val close_date: Long? = null,
    /**
     * *Optional*. Description of the poll; for polls inside the [Message](https://core.telegram.org/bots/api/#message) object only
     */
    public val description: String? = null,
    /**
     * *Optional*. Special entities like usernames, URLs, bot commands, etc. that appear in the description
     */
    public val description_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Media added to the poll description; for polls inside the [Message](https://core.telegram.org/bots/api/#message) object only
     */
    public val media: PollMedia? = null,
)
