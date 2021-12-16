package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

public class DeleteMessage(
    private val chatId: String,
    private val messageId: Int,
) : ApiMethod<Boolean> {
    @Serializable
    public data class Request(
        val chat_id: String,
        val message_id: Int,
    )

    private val deserializer: KSerializer<ApiResponse<Boolean>> =
        ApiResponse.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "deleteMessage",
            serialize = {
                json.encodeToString(
                    Request.serializer(),
                    Request(
                        chat_id = chatId,
                        message_id = messageId,
                    )
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
