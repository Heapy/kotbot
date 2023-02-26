package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.User
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a [User](https://core.telegram.org/bots/api/#user) object.
 */
@Serializable
public class GetMe : Method<GetMe, User> by Companion {
    public companion object : Method<GetMe, User> {
        override val _deserializer: KSerializer<Response<User>> =
                Response.serializer(User.serializer())

        override val _serializer: KSerializer<GetMe> = serializer()

        override val _name: String = "getMe"
    }
}
