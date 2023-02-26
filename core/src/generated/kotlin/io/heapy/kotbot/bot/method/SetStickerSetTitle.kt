package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set the title of a created sticker set. Returns *True* on success.
 */
@Serializable
public data class SetStickerSetTitle(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * Sticker set title, 1-64 characters
     */
    public val title: String,
) : Method<SetStickerSetTitle, Boolean> by Companion {
    public companion object : Method<SetStickerSetTitle, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetStickerSetTitle> = serializer()

        override val _name: String = "setStickerSetTitle"
    }
}
