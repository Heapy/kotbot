package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to remove webhook integration if you decide to switch
 * back to getUpdates. Returns True on success.
 */
@Serializable
public data class DeleteWebhook(
    /**
     * Pass True to drop all pending updates
     */
    val drop_pending_updates: Boolean? = null,
) : ApiMethod<Boolean> {
    @Transient
    private val deserializer: KSerializer<ApiResponse<Boolean>> =
        ApiResponse.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "deleteWebhook",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@DeleteWebhook
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}

