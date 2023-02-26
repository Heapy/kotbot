package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to delete a sticker set that was created by the bot. Returns *True* on success.
 */
@Serializable
public data class DeleteStickerSet(
    /**
     * Sticker set name
     */
    public val name: String,
) : Method<DeleteStickerSet, Boolean> by Companion {
    public companion object : Method<DeleteStickerSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteStickerSet> = serializer()

        override val _name: String = "deleteStickerSet"
    }
}
