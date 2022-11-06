package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 */
@Serializable
public data class MessageEntity(
    /**
     * Type of the entity. Currently, can be “mention” (`@username`), “hashtag” (`#hashtag`), “cashtag” (`$USD`), “bot_command” (`/start@jobs_bot`), “url” (`https://telegram.org`), “email” (`do-not-reply@telegram.org`), “phone_number” (`+1-212-555-0123`), “bold” (**bold text**), “italic” (*italic text*), “underline” (underlined text), “strikethrough” (strikethrough text), “spoiler” (spoiler message), “code” (monowidth string), “pre” (monowidth block), “text_link” (for clickable text URLs), “text_mention” (for users [without usernames](https://telegram.org/blog/edit#new-mentions)), “custom_emoji” (for inline custom emoji stickers)
     */
    public val type: String,
    /**
     * Offset in UTF-16 code units to the start of the entity
     */
    public val offset: Int,
    /**
     * Length of the entity in UTF-16 code units
     */
    public val length: Int,
    /**
     * *Optional*. For “text_link” only, URL that will be opened after user taps on the text
     */
    public val url: String? = null,
    /**
     * *Optional*. For “text_mention” only, the mentioned user
     */
    public val user: User? = null,
    /**
     * *Optional*. For “pre” only, the programming language of the entity text
     */
    public val language: String? = null,
    /**
     * *Optional*. For “custom_emoji” only, unique identifier of the custom emoji. Use [getCustomEmojiStickers](https://core.telegram.org/bots/api/#getcustomemojistickers) to get full information about the sticker
     */
    public val custom_emoji_id: String? = null,
)
