package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to delete a sticker from a set created by the bot. Returns *True* on success.
 */
@Serializable
public data class DeleteStickerFromSet(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
) : Method<DeleteStickerFromSet, Boolean> by Companion {
    public companion object : Method<DeleteStickerFromSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteStickerFromSet> = serializer()

        override val _name: String = "deleteStickerFromSet"
    }
}
