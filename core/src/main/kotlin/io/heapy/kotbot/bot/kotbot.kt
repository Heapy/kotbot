package io.heapy.kotbot.bot

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Workaround until [multi-receivers](https://youtrack.jetbrains.com/issue/KT-10468) will be implemented in Kotlin.
 */
public data class Kotbot(
    public val token: String,
    public val baseUrl: String = "https://api.telegram.org/bot",
    public val httpClient: HttpClient = HttpClient(CIO) {
        engine {
            requestTimeout = 60_000
        }
    },
    public val json: Json = Json {
        ignoreUnknownKeys = true
    }
)

public suspend inline fun <Response> Kotbot.execute(
    method: ApiMethod<Response>
): Response =
    with(method) {
        execute()
    }

public suspend fun Kotbot.receiveUpdates(
    timeout: Int = 50,
    limit: Int = 100,
    allowedUpdates: List<String> = listOf(),
): Flow<ApiUpdate> {
    var offset: Int? = null

    return flow {
        while (true) {
            val updates = execute(GetUpdates(
                offset = offset?.let { it + 1 },
                limit = limit,
                timeout = timeout,
                allowed_updates = allowedUpdates,
            ))

            offset = updates.maxByOrNull { it.update_id }?.update_id ?: offset

            updates.forEach { update ->
                LOGGER.debug("Received update: {}", update)
                emit(update)
            }
        }
    }
}

private inline fun <reified T : Any> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)

private val LOGGER = logger<Kotbot>()
