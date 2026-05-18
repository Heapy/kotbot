package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a sticker file to be sent.
 */
@Serializable
public data class InputMediaSticker(
    /**
     * Type of the result, must be *sticker*
     */
    public val type: String,
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a .WEBP sticker from the Internet, or pass "attach://<file_attach_name>" to upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Emoji associated with the sticker; only for just uploaded stickers
     */
    public val emoji: String? = null,
) : InputPollOptionMedia
