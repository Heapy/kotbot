package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to move a sticker in a set created by the bot to a specific position. Returns *True* on success.
 */
@Serializable
public data class SetStickerPositionInSet(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * New sticker position in the set, zero-based
     */
    public val position: Int,
) : Method<SetStickerPositionInSet, Boolean> by Companion {
    public companion object : Method<SetStickerPositionInSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetStickerPositionInSet> = serializer()

        override val _name: String = "setStickerPositionInSet"
    }
}
