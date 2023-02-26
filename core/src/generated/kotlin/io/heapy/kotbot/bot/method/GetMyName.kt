package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotName
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current bot name for the given user language. Returns [BotName](https://core.telegram.org/bots/api/#botname) on success.
 */
@Serializable
public data class GetMyName(
    /**
     * A two-letter ISO 639-1 language code or an empty string
     */
    public val language_code: String? = null,
) : Method<GetMyName, BotName> by Companion {
    public companion object : Method<GetMyName, BotName> {
        override val _deserializer: KSerializer<Response<BotName>> =
                Response.serializer(BotName.serializer())

        override val _serializer: KSerializer<GetMyName> = serializer()

        override val _name: String = "getMyName"
    }
}
