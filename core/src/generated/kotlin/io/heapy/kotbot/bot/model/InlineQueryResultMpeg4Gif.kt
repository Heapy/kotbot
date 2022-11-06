package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user with optional caption. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the animation.
 */
@Serializable
public data class InlineQueryResultMpeg4Gif(
    /**
     * Type of the result, must be *mpeg4_gif*
     */
    public val type: String = "mpeg4_gif",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL for the MPEG4 file. File size must not exceed 1MB
     */
    public val mpeg4_url: String,
    /**
     * *Optional*. Video width
     */
    public val mpeg4_width: Int? = null,
    /**
     * *Optional*. Video height
     */
    public val mpeg4_height: Int? = null,
    /**
     * *Optional*. Video duration in seconds
     */
    public val mpeg4_duration: Int? = null,
    /**
     * URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
     */
    public val thumb_url: String,
    /**
     * *Optional*. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
     */
    public val thumb_mime_type: String? = "image/jpeg",
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the video animation
     */
    public val input_message_content: InputMessageContent? = null,
) : InlineQueryResult
