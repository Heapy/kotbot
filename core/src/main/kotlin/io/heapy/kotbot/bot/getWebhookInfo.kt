package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.WebhookInfo
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Use this method to get current webhook status. Requires no parameters.
 * On success, returns a WebhookInfo object. If the bot is using
 * getUpdates, will return an object with the url field empty.
 */
@Serializable
public class GetWebhookInfo: Method<WebhookInfo> {
    @Transient
    private val deserializer: KSerializer<Response<WebhookInfo>> =
        Response.serializer(WebhookInfo.serializer())

    override suspend fun Kotbot.execute(): WebhookInfo {
        return requestForJson(
            name = "getWebhookInfo",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@GetWebhookInfo
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
