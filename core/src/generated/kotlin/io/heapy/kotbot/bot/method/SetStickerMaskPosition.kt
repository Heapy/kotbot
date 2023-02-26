package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MaskPosition
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the [mask position](https://core.telegram.org/bots/api/#maskposition) of a mask sticker. The sticker must belong to a sticker set that was created by the bot. Returns *True* on success.
 */
@Serializable
public data class SetStickerMaskPosition(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * A JSON-serialized object with the position where the mask should be placed on faces. Omit the parameter to remove the mask position.
     */
    public val mask_position: MaskPosition? = null,
) : Method<SetStickerMaskPosition, Boolean> by Companion {
    public companion object : Method<SetStickerMaskPosition, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetStickerMaskPosition> = serializer()

        override val _name: String = "setStickerMaskPosition"
    }
}
