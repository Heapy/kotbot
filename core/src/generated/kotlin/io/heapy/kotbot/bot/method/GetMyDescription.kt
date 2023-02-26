package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotDescription
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current bot description for the given user language. Returns [BotDescription](https://core.telegram.org/bots/api/#botdescription) on success.
 */
@Serializable
public data class GetMyDescription(
    /**
     * A two-letter ISO 639-1 language code or an empty string
     */
    public val language_code: String? = null,
) : Method<GetMyDescription, BotDescription> by Companion {
    public companion object : Method<GetMyDescription, BotDescription> {
        override val _deserializer: KSerializer<Response<BotDescription>> =
                Response.serializer(BotDescription.serializer())

        override val _serializer: KSerializer<GetMyDescription> = serializer()

        override val _name: String = "getMyDescription"
    }
}
