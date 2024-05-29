package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InputPollOption
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.ReplyMarkup
import io.heapy.kotbot.bot.model.ReplyParameters
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to send a native poll. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendPoll(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    public val business_connection_id: String? = null,
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * Poll question, 1-300 characters
     */
    public val question: String,
    /**
     * Mode for parsing entities in the question. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details. Currently, only custom emoji entities are allowed
     */
    public val question_parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the poll question. It can be specified instead of *question_parse_mode*
     */
    public val question_entities: List<MessageEntity>? = null,
    /**
     * A JSON-serialized list of 2-10 answer options
     */
    public val options: List<InputPollOption>,
    /**
     * *True*, if the poll needs to be anonymous, defaults to *True*
     */
    public val is_anonymous: Boolean? = null,
    /**
     * Poll type, "quiz" or "regular", defaults to "regular"
     */
    public val type: String? = null,
    /**
     * *True*, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to *False*
     */
    public val allows_multiple_answers: Boolean? = null,
    /**
     * 0-based identifier of the correct answer option, required for polls in quiz mode
     */
    public val correct_option_id: Int? = null,
    /**
     * Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing
     */
    public val explanation: String? = null,
    /**
     * Mode for parsing entities in the explanation. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val explanation_parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the poll explanation. It can be specified instead of *explanation_parse_mode*
     */
    public val explanation_entities: List<MessageEntity>? = null,
    /**
     * Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with *close_date*.
     */
    public val open_period: Int? = null,
    /**
     * Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with *open_period*.
     */
    public val close_date: Long? = null,
    /**
     * Pass *True* if the poll needs to be immediately closed. This can be useful for poll preview.
     */
    public val is_closed: Boolean? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; for private chats only
     */
    public val message_effect_id: String? = null,
    /**
     * Description of the message to reply to
     */
    public val reply_parameters: ReplyParameters? = null,
    /**
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
     */
    public val reply_markup: ReplyMarkup? = null,
) : Method<SendPoll, Message> by Companion {
    public companion object : Method<SendPoll, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<SendPoll> = serializer()

        override val _name: String = "sendPoll"
    }
}
