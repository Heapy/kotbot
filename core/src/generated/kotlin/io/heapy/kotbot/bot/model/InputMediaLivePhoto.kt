package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a live photo to be sent.
 */
@Serializable
public data class InputMediaLivePhoto(
    /**
     * Type of the result, must be *live_photo*
     */
    public val type: String,
    /**
     * Video of the live photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files). Sending live photos by a URL is currently unsupported.
     */
    public val media: String,
    /**
     * The static photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files). Sending live photos by a URL is currently unsupported.
     */
    public val photo: String,
    /**
     * *Optional*. Caption of the live photo to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the live photo caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
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
     * *Optional*. Pass *True* if the live photo needs to be covered with a spoiler animation
     */
    public val has_spoiler: Boolean? = null,
) : InputPollMedia,
    InputPollOptionMedia,
    InputMedia
