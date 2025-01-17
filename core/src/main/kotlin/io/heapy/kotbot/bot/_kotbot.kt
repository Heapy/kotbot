package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.GetUpdates
import io.heapy.kotbot.bot.model.Update
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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
    public val json: Json = kotbotJson,
)

public val kotbotJson: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
}

public fun Kotbot.receiveUpdates(
    timeout: Int = 50,
    limit: Int = 100,
    allowedUpdates: List<String> = listOf(),
): Flow<Update> {
    var offset: Int? = null

    return flow {
        while (true) {
            val currentOffset = offset?.let { it + 1 }
            log.info("Receiving updates with offset $currentOffset")
            val updates = execute(GetUpdates(
                offset = currentOffset,
                limit = limit,
                timeout = timeout,
                allowed_updates = allowedUpdates,
            ))

            offset = updates.maxByOrNull { it.update_id }?.update_id ?: offset

            updates.forEach { update ->
                log.debug("Received update: {}", update)
                emit(update)
            }
        }
    }
}

public suspend inline fun <Request : Method<Request, Result>, Result> Kotbot.execute(
    method: Request,
): Result =
    requestForJson(
        name = method._name,
        serialize = {
            json
                .encodeToString(
                    method._serializer,
                    method,
                )
        },
        deserialize = {
            json
                .decodeFromString(
                    method._deserializer,
                    it.bodyAsText(),
                )
                .unwrap()
        },
    )

public interface Method<Request, Result> {
    public val _serializer: KSerializer<Request>
    public val _deserializer: KSerializer<Response<Result>>
    public val _name: String
}

@Serializable
public data class Response<Result>(
    public val ok: Boolean,
    public val result: Result? = null,
    public val error_code: Int? = null,
    public val description: String? = null,
    public val parameters: ResponseParameters? = null,
)

@Serializable
public data class ResponseParameters(
    public val migrate_to_chat_id: Long? = null,
    public val retry_after: Int? = null,
)

public fun <T> Response<T>.unwrap(): T {
    if (ok) {
        return result ?: throw KotbotException("Response is ok but result is null")
    } else {
        throw TelegramApiError(
            message = "Telegram API error",
            errorCode = error_code,
            description = description,
            migrateToChatId = parameters?.migrate_to_chat_id,
            retryAfter = parameters?.retry_after,
        )
    }
}

public class TelegramApiError(
    message: String,
    public val errorCode: Int? = null,
    public val description: String? = null,
    public val migrateToChatId: Long? = null,
    public val retryAfter: Int? = null,
) : RuntimeException(message)

public class KotbotException(
    message: String,
) : RuntimeException(message)

public suspend inline fun <Response> Kotbot.requestForJson(
    name: String,
    serialize: () -> String,
    deserialize: (HttpResponse) -> Response,
): Response {
    val response = httpClient
        .post("$baseUrl$token/$name") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(serialize())
        }

    return if (response.status.isSuccess()) {
        deserialize(response)
    } else {
        throw TelegramApiError("${response.status} ${response.bodyAsText()}")
    }
}

internal inline fun <reified T : Any> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)

private val log = logger<Kotbot>()
