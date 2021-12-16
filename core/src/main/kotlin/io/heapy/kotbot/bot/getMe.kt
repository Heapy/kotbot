package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer

/**
 * A simple method for testing your bot's authentication token.
 * Requires no parameters. Returns basic information about the bot in form
 * of a [User] object.
 */
public class GetMe : ApiMethod<User> {
    private val deserializer: KSerializer<ApiResponse<User>> =
        ApiResponse.serializer(User.serializer())

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
