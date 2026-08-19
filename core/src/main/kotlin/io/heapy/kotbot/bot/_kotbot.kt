package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.GetUpdates
import io.heapy.kotbot.bot.model.Update
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public class Kotbot(
    public val token: String,
    public val baseUrl: String = "https://api.telegram.org/bot",
    public val httpClient: HttpClient = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
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
    retryDelay: Duration = 1.seconds,
): Flow<List<Update>> {
    var offset: Int? = null

    return flow {
        while (true) {
            val currentOffset = offset?.let { it + 1 }
            log.info("Receiving updates with offset $currentOffset")
            val updates = try {
                execute(
                    GetUpdates(
                        offset = currentOffset,
                        limit = limit,
                        timeout = timeout,
                        allowed_updates = allowedUpdates,
                    ),
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // A long poll that outruns the client deadline is normal Telegram behaviour, not a
                // fault: Telegram caps the poll at ~50s regardless of the requested timeout and
                // answers late often enough to matter -- frame-level measurement put roughly a
                // sixth of all polls at 65-100s, and the reference Go bot on the same host sees
                // the same spread but sets no deadline at all, so it never reports anything. The
                // retry below recovers in one second and no update is lost, because the offset
                // only advances on a delivered batch. Logging that at ERROR with a stack trace
                // buried real failures under a steady stream of identical noise.
                //
                // The exception message is deliberately not logged: it embeds the request URL,
                // which carries the bot token.
                if (e is HttpRequestTimeoutException || e is SocketTimeoutException) {
                    log.warn(
                        "getUpdates timed out ({}), retrying in {}",
                        e::class.simpleName,
                        retryDelay,
                    )
                } else {
                    log.error("getUpdates failed, retrying in {}", retryDelay, e)
                }
                delay(retryDelay)
                continue
            }

            offset = updates.maxByOrNull { it.update_id }?.update_id ?: offset

            emit(updates)
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
    @SerialName("error_code")
    public val errorCode: Int? = null,
    public val description: String? = null,
    public val parameters: ResponseParameters? = null,
)

@Serializable
public data class ResponseParameters(
    @SerialName("migrate_to_chat_id")
    public val migrateToChatId: Long? = null,
    @SerialName("retry_after")
    public val retryAfter: Int? = null,
)

public fun <T> Response<T>.unwrap(): T {
    if (ok) {
        return result ?: throw KotbotException("Response is ok but result is null")
    } else {
        throw TelegramApiError(
            message = "Telegram API error",
            errorCode = errorCode,
            description = description,
            migrateToChatId = parameters?.migrateToChatId,
            retryAfter = parameters?.retryAfter,
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
