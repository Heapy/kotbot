package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a sticker set.
 */
@Serializable
public data class StickerSet(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * Sticker set title
     */
    public val title: String,
    /**
     * Type of stickers in the set, currently one of "regular", "mask", "custom_emoji"
     */
    public val sticker_type: String,
    /**
     * List of all set stickers
     */
    public val stickers: List<Sticker>,
    /**
     * *Optional*. Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format
     */
    public val thumbnail: PhotoSize? = null,
)
