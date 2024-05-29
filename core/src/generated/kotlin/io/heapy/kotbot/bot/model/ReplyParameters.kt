package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes reply parameters for the message that is being sent.
 */
@Serializable
public data class ReplyParameters(
    /**
     * Identifier of the message that will be replied to in the current chat, or in the chat *chat_id* if it is specified
     */
    public val message_id: Int,
    /**
     * *Optional*. If the message to be replied to is from a different chat, unique identifier for the chat or username of the channel (in the format `@channelusername`). Not supported for messages sent on behalf of a business account.
     */
    public val chat_id: ChatId? = null,
    /**
     * *Optional*. Pass *True* if the message should be sent even if the specified message to be replied to is not found. Always *False* for replies in another chat or forum topic. Always *True* for messages sent on behalf of a business account.
     */
    public val allow_sending_without_reply: Boolean? = null,
    /**
     * *Optional*. Quoted part of the message to be replied to; 0-1024 characters after entities parsing. The quote must be an exact substring of the message to be replied to, including *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, and *custom_emoji* entities. The message will fail to send if the quote isn't found in the original message.
     */
    public val quote: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the quote. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val quote_parse_mode: String? = null,
    /**
     * *Optional*. A JSON-serialized list of special entities that appear in the quote. It can be specified instead of *quote_parse_mode*.
     */
    public val quote_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Position of the quote in the original message in UTF-16 code units
     */
    public val quote_position: Int? = null,
)
