package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a sticker.
 */
@Serializable
public data class Sticker(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    public val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    public val file_unique_id: String,
    /**
     * Type of the sticker, currently one of "regular", "mask", "custom_emoji". The type of the sticker is independent from its format, which is determined by the fields *is_animated* and *is_video*.
     */
    public val type: String,
    /**
     * Sticker width
     */
    public val width: Int,
    /**
     * Sticker height
     */
    public val height: Int,
    /**
     * *True*, if the sticker is [animated](https://telegram.org/blog/animated-stickers)
     */
    public val is_animated: Boolean,
    /**
     * *True*, if the sticker is a [video sticker](https://telegram.org/blog/video-stickers-better-reactions)
     */
    public val is_video: Boolean,
    /**
     * *Optional*. Sticker thumbnail in the .WEBP or .JPG format
     */
    public val thumbnail: PhotoSize? = null,
    /**
     * *Optional*. Emoji associated with the sticker
     */
    public val emoji: String? = null,
    /**
     * *Optional*. Name of the sticker set to which the sticker belongs
     */
    public val set_name: String? = null,
    /**
     * *Optional*. For premium regular stickers, premium animation for the sticker
     */
    public val premium_animation: File? = null,
    /**
     * *Optional*. For mask stickers, the position where the mask should be placed
     */
    public val mask_position: MaskPosition? = null,
    /**
     * *Optional*. For custom emoji stickers, unique identifier of the custom emoji
     */
    public val custom_emoji_id: String? = null,
    /**
     * *Optional*. *True*, if the sticker must be repainted to a text color in messages, the color of the Telegram Premium badge in emoji status, white color on chat photos, or another appropriate color in other places
     */
    public val needs_repainting: Boolean? = true,
    /**
     * *Optional*. File size in bytes
     */
    public val file_size: Long? = null,
)
