package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to specify a url and receive incoming updates via an
 * outgoing webhook. Whenever there is an update for the bot, we will
 * send an HTTPS POST request to the specified url, containing
 * a JSON-serialized Update. In case of an unsuccessful request,
 * we will give up after a reasonable amount of attempts.
 * Returns True on success.
 *
 * If you'd like to make sure that the Webhook request comes from Telegram,
 * we recommend using a secret path in the URL,
 * e.g. https://www.example.com/<token>. Since nobody else knows your
 * bot's token, you can be pretty sure it's us.
 */
@Serializable
public data class SetWebhook(
    /**
     * HTTPS url to send updates to.
     * Use an empty string to remove webhook integration
     */
    val url: String,
    /**
     * Upload your public key certificate so that the root certificate
     * in use can be checked. See our self-signed guide for details.
     */
    val certificate: InputFile? = null,
    /**
     * The fixed IP address which will be used to send webhook requests
     * instead of the IP address resolved through DNS
     */
    val ip_address: String? = null,
    /**
     * Maximum allowed number of simultaneous HTTPS connections to the
     * webhook for update delivery, 1-100. Defaults to 40. Use lower values
     * to limit the load on your bot's server, and higher values to
     * increase your bot's throughput.
     */
    val max_connections: Int? = null,
    /**
     * A JSON-serialized list of the update types you want your bot to receive.
     * For example, specify [“message”, “edited_channel_post”, “callback_query”]
     * to only receive updates of these types. See Update for a complete list of
     * available update types. Specify an empty list to receive all update types
     * except chat_member (default). If not specified, the previous setting will
     * be used.
     *
     * Please note that this parameter doesn't affect updates created before the
     * call to the setWebhook, so unwanted updates may be received for a short
     * period of time.
     */
    val allowed_updates: List<String>? = null,
    /**
     * Pass True to drop all pending updates
     */
    val drop_pending_updates: Boolean? = null,
) : Method<Boolean> {
    @Transient
    private val deserializer: KSerializer<Response<Boolean>> =
        Response.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "setWebhook",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@SetWebhook
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
