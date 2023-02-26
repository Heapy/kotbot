package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns *True* on success.
 */
@Serializable
public data class SetStickerEmojiList(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * A JSON-serialized list of 1-20 emoji associated with the sticker
     */
    public val emoji_list: List<String>,
) : Method<SetStickerEmojiList, Boolean> by Companion {
    public companion object : Method<SetStickerEmojiList, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetStickerEmojiList> = serializer()

        override val _name: String = "setStickerEmojiList"
    }
}
