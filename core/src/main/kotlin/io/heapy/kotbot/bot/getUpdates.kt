package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

public class GetUpdates(
    private val offset: Int? = null,
    private val limit: Int? = null,
    private val timeout: Int? = null,
    private val allowed_updates: List<String>? = null,
) : ApiMethod<List<ApiUpdate>> {
    @Serializable
    public data class Request(
        val offset: Int? = null,
        val limit: Int? = null,
        val timeout: Int? = null,
        val allowed_updates: List<String>? = null,
    )

    private val deserializer: KSerializer<ApiResponse<List<ApiUpdate>>> =
        ApiResponse.serializer(ListSerializer(ApiUpdate.serializer()))

    override suspend fun Kotbot.execute(): List<ApiUpdate> {
        return requestForJson(
            name = "getUpdates",
            serialize = {
                json.encodeToString(
                    Request.serializer(),
                    Request(
                        offset = offset,
                        limit = limit,
                        timeout = timeout,
                        allowed_updates = allowed_updates,
                    )
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
