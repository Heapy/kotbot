package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The paid media to send is a live photo.
 */
@Serializable
public data class InputPaidMediaLivePhoto(
    /**
     * Type of the media, must be *live_photo*
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
) : InputPaidMedia
