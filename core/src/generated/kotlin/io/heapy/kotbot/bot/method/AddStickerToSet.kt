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
 * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers. Other sticker sets can have up to 120 stickers. Returns *True* on success.
 */
@Serializable
public data class AddStickerToSet(
    /**
     * User identifier of sticker set owner
     */
    public val user_id: Long,
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set isn't changed.
     */
    public val sticker: InputSticker,
) : Method<AddStickerToSet, Boolean> by Companion {
    public companion object : Method<AddStickerToSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<AddStickerToSet> = serializer()

        override val _name: String = "addStickerToSet"
    }
}
