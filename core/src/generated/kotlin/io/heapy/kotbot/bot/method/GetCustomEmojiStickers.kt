package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Sticker
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
 */
@Serializable
public data class GetCustomEmojiStickers(
    /**
     * List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
     */
    public val custom_emoji_ids: List<String>,
) : Method<GetCustomEmojiStickers, List<Sticker>> by Companion {
    public companion object : Method<GetCustomEmojiStickers, List<Sticker>> {
        override val _deserializer: KSerializer<Response<List<Sticker>>> =
                Response.serializer(ListSerializer(Sticker.serializer()))

        override val _serializer: KSerializer<GetCustomEmojiStickers> = serializer()

        override val _name: String = "getCustomEmojiStickers"
    }
}
