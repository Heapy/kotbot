package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object describes a sticker to be added to a sticker set.
 */
@Serializable
public data class InputSticker(
    /**
     * The added sticker. Pass a *file_id* as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, upload a new one using multipart/form-data, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. Animated and video stickers can't be uploaded via HTTP URL. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val sticker: Sticker,
    /**
     * Format of the added sticker, must be one of "static" for a **.WEBP** or **.PNG** image, "animated" for a **.TGS** animation, "video" for a **.WEBM** video
     */
    public val format: String,
    /**
     * List of 1-20 emoji associated with the sticker
     */
    public val emoji_list: List<String>,
    /**
     * *Optional*. Position where the mask should be placed on faces. For "mask" stickers only.
     */
    public val mask_position: MaskPosition? = null,
    /**
     * *Optional*. List of 0-20 search keywords for the sticker with total length of up to 64 characters. For "regular" and "custom_emoji" stickers only.
     */
    public val keywords: List<String>? = null,
)
