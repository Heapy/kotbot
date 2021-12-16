package io.heapy.kotbot.bot

import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import java.lang.RuntimeException

public interface ApiMethod<Response> {
    public suspend fun Kotbot.execute(): Response
}

@Serializable
public data class ApiResponse<Result>(
    public val ok: Boolean,
    public val result: Result? = null,
    public val error_code: Int? = null,
    public val description: String? = null,
    public val parameters: ResponseParameters? = null,
)

@Serializable
public data class ResponseParameters(
    public val migrate_to_chat_id: Long?,
    public val retry_after: Int?,
)

internal fun <T> ApiResponse<T>.unwrap(): T {
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

internal suspend inline fun <Response> Kotbot.requestForJson(
    name: String,
    serialize: () -> String,
    deserialize: (HttpResponse) -> Response
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
