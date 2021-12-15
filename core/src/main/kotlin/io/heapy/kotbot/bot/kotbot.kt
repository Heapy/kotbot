package io.heapy.kotbot.bot

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

private val LOGGER = logger<Kotbot>()

public suspend inline fun <reified Req, reified Res> Kotbot.execute(method: ApiMethod<Req, Res>): Res {
    val response = httpClient
        .post("$baseUrl$token/${method.name}") {
            header("charset", StandardCharsets.UTF_8.name())
            header("Content-Type", "application/json")
            setBody(json.encodeToString(method.serializer, method.request))
        }

    return if (response.status.isSuccess()) {
        val data = json.decodeFromString(
            ApiResponse.serializer(method.deserializer),
            response.bodyAsText()
        )
        data.result!!
    } else {
        error("${response.status} ${response.bodyAsText()}")
    }
}

public suspend fun Kotbot.receiveUpdates(
    timeout: Int = 50,
    limit: Int = 100,
    allowedUpdates: List<String> = listOf(),
) : Flow<ApiUpdate> {
    var lastReceivedUpdate = 0

    return flow {
        while (true) {
            val updates = execute(GetUpdates(
                offset = lastReceivedUpdate + 1,
                limit = limit,
                timeout = timeout,
                allowed_updates = allowedUpdates,
            ))

            lastReceivedUpdate = updates.maxByOrNull { it.update_id }?.update_id ?: 0

            updates.forEach { update ->
                emit(update)
            }
        }
    }
}

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

private inline fun <reified T : Any> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)
