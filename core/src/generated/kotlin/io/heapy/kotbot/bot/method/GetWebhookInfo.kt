package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.WebhookInfo
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get current webhook status. Requires no parameters. On success, returns a [WebhookInfo](https://core.telegram.org/bots/api/#webhookinfo) object. If the bot is using [getUpdates](https://core.telegram.org/bots/api/#getupdates), will return an object with the *url* field empty.
 */
@Serializable
public class GetWebhookInfo : Method<WebhookInfo> {
    public override suspend fun Kotbot.execute(): WebhookInfo = requestForJson(
        name = "getWebhookInfo",
        serialize = {
            json.encodeToString(
                serializer(),
                this@GetWebhookInfo
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<WebhookInfo>> =
                Response.serializer(WebhookInfo.serializer())
    }
}
