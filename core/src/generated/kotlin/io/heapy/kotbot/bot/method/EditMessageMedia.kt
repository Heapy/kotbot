package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.InputMedia
import io.heapy.kotbot.bot.model.MessageOrTrue
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to edit animation, audio, document, photo, or video messages, or to add media to text messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within **48 hours** from the time they were sent.
 */
@Serializable
public data class EditMessageMedia(
    /**
     * Unique identifier of the business connection on behalf of which the message to be edited was sent
     */
    public val business_connection_id: String? = null,
    /**
     * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId? = null,
    /**
     * Required if *inline_message_id* is not specified. Identifier of the message to edit
     */
    public val message_id: Int? = null,
    /**
     * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    public val inline_message_id: String? = null,
    /**
     * A JSON-serialized object for a new media content of the message
     */
    public val media: InputMedia,
    /**
     * A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<EditMessageMedia, MessageOrTrue> by Companion {
    public companion object : Method<EditMessageMedia, MessageOrTrue> {
        override val _deserializer: KSerializer<Response<MessageOrTrue>> =
                Response.serializer(MessageOrTrue.serializer())

        override val _serializer: KSerializer<EditMessageMedia> = serializer()

        override val _name: String = "editMessageMedia"
    }
}
