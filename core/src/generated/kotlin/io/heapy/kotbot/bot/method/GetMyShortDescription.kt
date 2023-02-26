package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotShortDescription
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current bot short description for the given user language. Returns [BotShortDescription](https://core.telegram.org/bots/api/#botshortdescription) on success.
 */
@Serializable
public data class GetMyShortDescription(
    /**
     * A two-letter ISO 639-1 language code or an empty string
     */
    public val language_code: String? = null,
) : Method<GetMyShortDescription, BotShortDescription> by Companion {
    public companion object : Method<GetMyShortDescription, BotShortDescription> {
        override val _deserializer: KSerializer<Response<BotShortDescription>> =
                Response.serializer(BotShortDescription.serializer())

        override val _serializer: KSerializer<GetMyShortDescription> = serializer()

        override val _name: String = "getMyShortDescription"
    }
}
