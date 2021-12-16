package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
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
