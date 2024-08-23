package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The paid media to send is a photo.
 */
@Serializable
public data class InputPaidMediaPhoto(
    /**
     * Type of the media, must be *photo*
     */
    public val type: String,
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val media: String,
) : InputPaidMedia
