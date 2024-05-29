package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputSticker
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to replace an existing sticker in a sticker set with a new one. The method is equivalent to calling [deleteStickerFromSet](https://core.telegram.org/bots/api/#deletestickerfromset), then [addStickerToSet](https://core.telegram.org/bots/api/#addstickertoset), then [setStickerPositionInSet](https://core.telegram.org/bots/api/#setstickerpositioninset). Returns *True* on success.
 */
@Serializable
public data class ReplaceStickerInSet(
    /**
     * User identifier of the sticker set owner
     */
    public val user_id: Long,
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * File identifier of the replaced sticker
     */
    public val old_sticker: String,
    /**
     * A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set remains unchanged.
     */
    public val sticker: InputSticker,
) : Method<ReplaceStickerInSet, Boolean> by Companion {
    public companion object : Method<ReplaceStickerInSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<ReplaceStickerInSet> = serializer()

        override val _name: String = "replaceStickerInSet"
    }
}
