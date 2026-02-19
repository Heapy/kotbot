package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Converts a given regular gift to Telegram Stars. Requires the *can_convert_gifts_to_stars* business bot right. Returns *True* on success.
 */
@Serializable
public data class ConvertGiftToStars(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Unique identifier of the regular gift that should be converted to Telegram Stars
     */
    public val owned_gift_id: String,
) : Method<ConvertGiftToStars, Boolean> by Companion {
    public companion object : Method<ConvertGiftToStars, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<ConvertGiftToStars> = serializer()

        override val _name: String = "convertGiftToStars"
    }
}
