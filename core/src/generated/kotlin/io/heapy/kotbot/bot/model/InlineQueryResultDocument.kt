package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a file. By default, this file will be sent by the user with an optional caption. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the file. Currently, only **.PDF** and **.ZIP** files can be sent using this method.
 */
@Serializable
public data class InlineQueryResultDocument(
    /**
     * Type of the result, must be *document*
     */
    public val type: String,
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * Title for the result
     */
    public val title: String,
    /**
     * *Optional*. Caption of the document to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the document caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * A valid URL for the file
     */
    public val document_url: String,
    /**
     * MIME type of the content of the file, either "application/pdf" or "application/zip"
     */
    public val mime_type: String,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
    /**
     * *Optional*. Inline keyboard attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the file
     */
    public val input_message_content: InputMessageContent? = null,
    /**
     * *Optional*. URL of the thumbnail (JPEG only) for the file
     */
    public val thumbnail_url: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    public val thumbnail_width: Int? = null,
    /**
     * *Optional*. Thumbnail height
     */
    public val thumbnail_height: Int? = null,
) : InlineQueryResult
