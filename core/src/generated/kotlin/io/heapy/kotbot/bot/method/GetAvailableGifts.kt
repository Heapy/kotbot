package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Gifts
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Returns the list of gifts that can be sent by the bot to users. Requires no parameters. Returns a [Gifts](https://core.telegram.org/bots/api/#gifts) object.
 */
@Serializable
public class GetAvailableGifts : Method<GetAvailableGifts, Gifts> by Companion {
    public companion object : Method<GetAvailableGifts, Gifts> {
        override val _deserializer: KSerializer<Response<Gifts>> =
                Response.serializer(Gifts.serializer())

        override val _serializer: KSerializer<GetAvailableGifts> = serializer()

        override val _name: String = "getAvailableGifts"
    }
}
