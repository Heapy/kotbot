package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.MessageOrTrue
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within **48 hours** from the time they were sent.
 */
@Serializable
public data class EditMessageCaption(
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
     * New caption of the message, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the message caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * Pass *True*, if the caption must be shown above the message media. Supported only for animation, photo and video messages.
     */
    public val show_caption_above_media: Boolean? = null,
    /**
     * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<EditMessageCaption, MessageOrTrue> by Companion {
    public companion object : Method<EditMessageCaption, MessageOrTrue> {
        override val _deserializer: KSerializer<Response<MessageOrTrue>> =
                Response.serializer(MessageOrTrue.serializer())

        override val _serializer: KSerializer<EditMessageCaption> = serializer()

        override val _name: String = "editMessageCaption"
    }
}
