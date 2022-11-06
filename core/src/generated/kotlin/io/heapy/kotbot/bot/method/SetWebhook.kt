package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputFile
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized [Update](https://core.telegram.org/bots/api/#update). In case of an unsuccessful request, we will give up after a reasonable amount of attempts. Returns *True* on success.
 *
 * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter *secret_token*. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
 */
@Serializable
public data class SetWebhook(
    /**
     * HTTPS URL to send updates to. Use an empty string to remove webhook integration
     */
    public val url: String,
    /**
     * Upload your public key certificate so that the root certificate in use can be checked. See our [self-signed guide](https://core.telegram.org/bots/self-signed) for details.
     */
    public val certificate: InputFile? = null,
    /**
     * The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
     */
    public val ip_address: String? = null,
    /**
     * The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to *40*. Use lower values to limit the load on your bot's server, and higher values to increase your bot's throughput.
     */
    public val max_connections: Int? = 40,
    /**
     * A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”, “edited_channel_post”, “callback_query”] to only receive updates of these types. See [Update](https://core.telegram.org/bots/api/#update) for a complete list of available update types. Specify an empty list to receive all update types except *chat_member* (default). If not specified, the previous setting will be used.  
     * Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted updates may be received for a short period of time.
     */
    public val allowed_updates: List<String>? = null,
    /**
     * Pass *True* to drop all pending updates
     */
    public val drop_pending_updates: Boolean? = null,
    /**
     * A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook request, 1-256 characters. Only characters `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed. The header is useful to ensure that the request comes from a webhook set by you.
     */
    public val secret_token: String? = null,
) : Method<Boolean> {
    public override suspend fun Kotbot.execute(): Boolean = requestForJson(
        name = "setWebhook",
        serialize = {
            json.encodeToString(
                serializer(),
                this@SetWebhook
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())
    }
}
