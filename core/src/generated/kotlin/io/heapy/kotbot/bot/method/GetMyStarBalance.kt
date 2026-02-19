package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.StarAmount
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * A method to get the current Telegram Stars balance of the bot. Requires no parameters. On success, returns a [StarAmount](https://core.telegram.org/bots/api/#staramount) object.
 */
@Serializable
public class GetMyStarBalance : Method<GetMyStarBalance, StarAmount> by Companion {
    public companion object : Method<GetMyStarBalance, StarAmount> {
        override val _deserializer: KSerializer<Response<StarAmount>> =
                Response.serializer(StarAmount.serializer())

        override val _serializer: KSerializer<GetMyStarBalance> = serializer()

        override val _name: String = "getMyStarBalance"
    }
}
