package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set the thumbnail of a custom emoji sticker set. Returns *True* on success.
 */
@Serializable
public data class SetCustomEmojiStickerSetThumbnail(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
     */
    public val custom_emoji_id: String? = null,
) : Method<SetCustomEmojiStickerSetThumbnail, Boolean> by Companion {
    public companion object : Method<SetCustomEmojiStickerSetThumbnail, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetCustomEmojiStickerSetThumbnail> = serializer()

        override val _name: String = "setCustomEmojiStickerSetThumbnail"
    }
}
