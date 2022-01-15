package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Use this method to send text messages.
 * On success, the sent [Update.Message] is returned.
 */
@Serializable
public data class SendMessage(
    /**
     * Unique identifier for the target chat or username of the target
     * channel (in the format @channelusername)
     */
    private val chat_id: String,
    /**
     * Text of the message to be sent, 1-4096 characters after entities parsing
     */
    private val text: String,
    /**
     * Mode for parsing entities in the message text.
     * See formatting options for more details.
     */
    private val parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in message text,
     * which can be specified instead of parse_mode
     */
    private val entities: List<Update.MessageEntity>? = null,
    /**
     * Disables link previews for links in this message
     */
    private val disable_web_page_preview: Boolean? = null,
    /**
     * Sends the message silently. Users will receive a notification
     * with no sound.
     */
    private val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    private val protect_content: Boolean? = null,
    /**
     * If the message is a reply, ID of the original message
     */
    private val reply_to_message_id: Int? = null,
    /**
     * Pass True, if the message should be sent even if the specified
     * replied-to message is not found
     */
    private val allow_sending_without_reply: Boolean? = null,
    /**
     * Additional interface options. A JSON-serialized object for an inline
     * keyboard, custom reply keyboard, instructions to remove reply
     * keyboard or to force a reply from the user.
     *
     * TODO: Wrong type
     */
    private val reply_markup: Update.InlineKeyboardMarkup? = null,

) : Method<Update.Message> {
    @Transient
    private val deserializer: KSerializer<Response<Update.Message>> =
        Response.serializer(Update.Message.serializer())

    override suspend fun Kotbot.execute(): Update.Message {
        return requestForJson(
            name = "sendMessage",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@SendMessage
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
