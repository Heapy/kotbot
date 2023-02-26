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
 * Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns *True* on success.
 */
@Serializable
public data class SetStickerKeywords(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * A JSON-serialized list of 0-20 search keywords for the sticker with total length of up to 64 characters
     */
    public val keywords: List<String>? = null,
) : Method<SetStickerKeywords, Boolean> by Companion {
    public companion object : Method<SetStickerKeywords, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetStickerKeywords> = serializer()

        override val _name: String = "setStickerKeywords"
    }
}
