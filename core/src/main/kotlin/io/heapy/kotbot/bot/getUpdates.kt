package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to receive incoming updates using long polling
 * ([wiki](https://en.wikipedia.org/wiki/Push_technology#Long_polling))
 * An Array of [Update] objects is returned.
 */
@Serializable
public data class GetUpdates(
    /**
     * Identifier of the first update to be returned. Must be greater by one
     * than the highest among the identifiers of previously received updates.
     * By default, updates starting with the earliest unconfirmed update are
     * returned. An update is considered confirmed as soon as getUpdates is
     * called with an offset higher than its update_id. The negative offset
     * can be specified to retrieve updates starting from -offset update
     * from the end of the updates queue. All previous updates will forgotten.
     */
    private val offset: Int? = null,
    /**
     * Limits the number of updates to be retrieved.
     * Values between 1-100 are accepted. Defaults to 100.
     */
    private val limit: Int? = null,
    /**
     * Timeout in seconds for long polling. Defaults to 0, i.e. usual
     * short polling. Should be positive, short polling should be used
     * for testing purposes only.
     */
    private val timeout: Int? = null,
    /**
     * A JSON-serialized list of the update types you want your bot to receive.
     * For example, specify [“message”, “edited_channel_post”, “callback_query”]
     * to only receive updates of these types. See Update for a complete list
     * of available update types. Specify an empty list to receive all update
     * types except chat_member (default). If not specified, the previous
     * setting will be used.
     *
     * Please note that this parameter doesn't affect updates created before
     * the call to the getUpdates, so unwanted updates may be received for
     * a short period of time.
     */
    private val allowed_updates: List<String>? = null,
) : Method<List<Update>> {
    @Transient
    private val deserializer: KSerializer<Response<List<Update>>> =
        Response.serializer(ListSerializer(Update.serializer()))

    override suspend fun Kotbot.execute(): List<Update> {
        return requestForJson(
            name = "getUpdates",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@GetUpdates
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
