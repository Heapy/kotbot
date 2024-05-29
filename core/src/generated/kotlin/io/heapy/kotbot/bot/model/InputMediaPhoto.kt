package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a photo to be sent.
 */
@Serializable
public data class InputMediaPhoto(
    /**
     * Type of the result, must be *photo*
     */
    public val type: String = "photo",
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val media: String,
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
     * *Optional*. Pass *True* if the photo needs to be covered with a spoiler animation
     */
    public val has_spoiler: Boolean? = null,
) : InputMedia
