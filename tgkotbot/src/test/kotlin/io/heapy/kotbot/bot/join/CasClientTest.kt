package io.heapy.kotbot.bot.join

import io.heapy.kotbot.infra.configuration.CasConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.IOException

class CasClientTest {
    private fun clientFrom(engine: MockEngine) =
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpTimeout)
        }

    private fun clientReturning(body: String) =
        clientFrom(
            MockEngine {
                respond(
                    content = body,
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json",
                    ),
                )
            },
        )

    private fun casClient(client: HttpClient, forceFlagged: Long? = null) =
        CasClient(client, CasConfiguration(forceFlagged = forceFlagged))

    @Test
    fun `force-flagged user is flagged without querying CAS`() = runBlocking {
        val engine = MockEngine { error("CAS must not be called for force-flagged users") }
        val result = casClient(clientFrom(engine), forceFlagged = 42L).check(42L)
        assertEquals(
            CasResult.Flagged(
                offenses = null,
                timeAdded = null,
                messages = listOf("Forced CAS flag via configuration (cas.forceFlagged)"),
            ),
            result,
        )
    }

    @Test
    fun `ok false is clean`() = runBlocking {
        val result = casClient(clientReturning("""{"ok":false}""")).check(1L)
        assertEquals(CasResult.Clean, result)
    }

    @Test
    fun `ok true with result is flagged`() = runBlocking {
        val body = """{"ok":true,"result":{"offenses":3,"time_added":"2020-01-01T00:00:00.000Z"}}"""
        val result = casClient(clientReturning(body)).check(1L)
        assertEquals(
            CasResult.Flagged(offenses = 3, timeAdded = "2020-01-01T00:00:00.000Z"),
            result,
        )
    }

    @Test
    fun `ok true without result detail is flagged with nulls`() = runBlocking {
        val result = casClient(clientReturning("""{"ok":true}""")).check(1L)
        assertEquals(CasResult.Flagged(offenses = null, timeAdded = null), result)
    }

    @Test
    fun `flagged result captures reasons and spam messages`() = runBlocking {
        val body =
            """{"ok":true,"result":{"reasons":[1],"offenses":1,"messages":["free vpn spam","join now"],"time_added":"2026-06-08T13:18:37.000Z"}}"""
        val result = casClient(clientReturning(body)).check(1L)
        assertEquals(
            CasResult.Flagged(
                offenses = 1,
                timeAdded = "2026-06-08T13:18:37.000Z",
                reasons = listOf(1),
                messages = listOf("free vpn spam", "join now"),
            ),
            result,
        )
    }

    @Test
    fun `network failure fails open as clean`() = runBlocking {
        val engine = MockEngine { throw IOException("connection reset") }
        val result = casClient(clientFrom(engine)).check(1L)
        assertEquals(CasResult.Clean, result)
    }
}
