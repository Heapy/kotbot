package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.GetUpdates
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class TelegramErrorResponseTest {
    @Test
    fun `non-success HTTP response maps retry-after`() = runTest {
        val error = executeFailure(
            status = HttpStatusCode.TooManyRequests,
            body = """
                {
                  "ok": false,
                  "error_code": 429,
                  "description": "Too Many Requests: retry later",
                  "parameters": {
                    "retry_after": 17
                  }
                }
            """.trimIndent(),
        )

        val telegramError = assertInstanceOf(TelegramApiError::class.java, error)
        assertEquals(429, telegramError.errorCode)
        assertEquals("Too Many Requests: retry later", telegramError.description)
        assertEquals(17, telegramError.retryAfter)
        assertEquals(null, telegramError.migrateToChatId)
        assertEquals(429, telegramError.httpStatusCode)
    }

    @Test
    fun `non-success migration response preserves a 64-bit chat id`() = runTest {
        val error = executeFailure(
            status = HttpStatusCode.BadRequest,
            body = """
                {
                  "ok": false,
                  "error_code": 400,
                  "description": "Bad Request: group chat was upgraded",
                  "parameters": {
                    "migrate_to_chat_id": -1001234567890
                  }
                }
            """.trimIndent(),
        )

        val telegramError = assertInstanceOf(TelegramApiError::class.java, error)
        assertEquals(400, telegramError.errorCode)
        assertEquals("Bad Request: group chat was upgraded", telegramError.description)
        assertEquals(null, telegramError.retryAfter)
        assertEquals(-1001234567890, telegramError.migrateToChatId)
        assertEquals(400, telegramError.httpStatusCode)
    }

    @Test
    fun `malformed non-success response retains HTTP status and raw body`() = runTest {
        val rawBody = "upstream proxy exploded"
        val failure = executeFailure(
            status = HttpStatusCode.BadGateway,
            body = rawBody,
        )

        val error = assertInstanceOf(TelegramApiError::class.java, failure)
        assertEquals(null, error.errorCode)
        assertEquals(null, error.description)
        assertEquals(null, error.retryAfter)
        assertEquals(null, error.migrateToChatId)
        assertEquals(502, error.httpStatusCode)
        assertEquals("502 Bad Gateway $rawBody", error.message)
    }

    private suspend fun executeFailure(
        status: HttpStatusCode,
        body: String,
    ): Throwable {
        val engine = MockEngine {
            respond(
                content = body,
                status = status,
                headers = jsonHeaders,
            )
        }
        val client = HttpClient(engine) {
            expectSuccess = true
        }

        return try {
            checkNotNull(
                runCatching {
                    Kotbot(token = "test-token", httpClient = client).execute(GetUpdates())
                }.exceptionOrNull(),
            )
        } finally {
            client.close()
        }
    }

    private companion object {
        val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}
