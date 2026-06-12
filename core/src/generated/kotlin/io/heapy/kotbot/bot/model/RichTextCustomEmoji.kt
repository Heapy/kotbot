package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A custom emoji.
 */
@Serializable
public data class RichTextCustomEmoji(
    /**
     * Type of the rich text, always "custom_emoji"
     */
    public val type: String,
    /**
     * Unique identifier of the custom emoji. Use [getCustomEmojiStickers](https://core.telegram.org/bots/api/#getcustomemojistickers) to get full information about the sticker.
     */
    public val custom_emoji_id: String,
    /**
     * Alternative emoji for the custom emoji
     */
    public val alternative_text: String,
) : RichText
