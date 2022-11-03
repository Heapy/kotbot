package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.ReplyMarkup
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to send a native poll. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendPoll(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Poll question, 1-300 characters
   */
  public val question: String,
  /**
   * A JSON-serialized list of answer options, 2-10 strings 1-100 characters each
   */
  public val options: List<String>,
  /**
   * *True*, if the poll needs to be anonymous, defaults to *True*
   */
  public val is_anonymous: Boolean? = true,
  /**
   * Poll type, “quiz” or “regular”, defaults to “regular”
   */
  public val type: String? = "regular",
  /**
   * *True*, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to *False*
   */
  public val allows_multiple_answers: Boolean? = false,
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
   * A JSON-serialized list of special entities that appear in the poll explanation, which can be specified instead of *parse_mode*
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
   * If the message is a reply, ID of the original message
   */
  public val reply_to_message_id: Int? = null,
  /**
   * Pass *True* if the message should be sent even if the specified replied-to message is not found
   */
  public val allow_sending_without_reply: Boolean? = null,
  /**
   * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating), [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
   */
  public val reply_markup: ReplyMarkup? = null,
) : Method<Message> {
  public override suspend fun Kotbot.execute(): Message = requestForJson(
    name = "sendPoll",
    serialize = {
      json.encodeToString(
        serializer(),
        this@SendPoll
      )
    },
    deserialize = {
      json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
    }
  )

  public companion object {
    public val deserializer: KSerializer<Response<Message>> =
        Response.serializer(Message.serializer())
  }
}
