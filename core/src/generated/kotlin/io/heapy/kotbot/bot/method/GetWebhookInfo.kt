package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.WebhookInfo
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get current webhook status. Requires no parameters. On success, returns a [WebhookInfo](https://core.telegram.org/bots/api/#webhookinfo) object. If the bot is using [getUpdates](https://core.telegram.org/bots/api/#getupdates), will return an object with the *url* field empty.
 */
@Serializable
public class GetWebhookInfo : Method<GetWebhookInfo, WebhookInfo> by Companion {
    public companion object : Method<GetWebhookInfo, WebhookInfo> {
        override val _deserializer: KSerializer<Response<WebhookInfo>> =
                Response.serializer(WebhookInfo.serializer())

        override val _serializer: KSerializer<GetWebhookInfo> = serializer()

        override val _name: String = "getWebhookInfo"
    }
}
