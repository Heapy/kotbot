package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a photo stored on the Telegram servers. By default, this photo will be sent by the user with an optional caption. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the photo.
 */
@Serializable
public data class InlineQueryResultCachedPhoto(
    /**
     * Type of the result, must be *photo*
     */
    public val type: String = "photo",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier of the photo
     */
    public val photo_file_id: String,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
    /**
     * *Optional*. Caption of the photo to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the photo caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Pass *True*, if the caption must be shown above the message media
     */
    public val show_caption_above_media: Boolean? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the photo
     */
    public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
