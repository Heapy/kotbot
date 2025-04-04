package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a link to an article or web page.
 */
@Serializable
public data class InlineQueryResultArticle(
    /**
     * Type of the result, must be *article*
     */
    public val type: String,
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Title of the result
     */
    public val title: String,
    /**
     * Content of the message to be sent
     */
    public val input_message_content: InputMessageContent,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. URL of the result
     */
    public val url: String? = null,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
    /**
     * *Optional*. Url of the thumbnail for the result
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
