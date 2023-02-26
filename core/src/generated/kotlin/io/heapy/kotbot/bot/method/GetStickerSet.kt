package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.StickerSet
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get a sticker set. On success, a [StickerSet](https://core.telegram.org/bots/api/#stickerset) object is returned.
 */
@Serializable
public data class GetStickerSet(
    /**
     * Name of the sticker set
     */
    public val name: String,
) : Method<GetStickerSet, StickerSet> by Companion {
    public companion object : Method<GetStickerSet, StickerSet> {
        override val _deserializer: KSerializer<Response<StickerSet>> =
                Response.serializer(StickerSet.serializer())

        override val _serializer: KSerializer<GetStickerSet> = serializer()

        override val _name: String = "getStickerSet"
    }
}
