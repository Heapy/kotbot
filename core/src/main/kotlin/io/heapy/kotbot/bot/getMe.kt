package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.User
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer

/**
 * A simple method for testing your bot's authentication token.
 * Requires no parameters. Returns basic information about the bot in form
 * of a [User] object.
 */
public class GetMe : Method<User> {
    private val deserializer: KSerializer<Response<User>> =
        Response.serializer(User.serializer())

    override suspend fun Kotbot.execute(): User {
        return requestForJson(
            name = "getMe",
            serialize = { "" },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
