package io.heapy.kotbot.bot.methods

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.receiveUpdates
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.milliseconds

class ReceiveUpdatesTest {
    @Test
    fun `recovers from a failed poll and keeps polling`() = runTest {
        val requestCount = AtomicInteger(0)
        val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

        val engine = MockEngine { _ ->
            when (requestCount.incrementAndGet()) {
                // First poll fails: this used to terminate the whole flow.
                1 -> respond("boom", HttpStatusCode.InternalServerError)
                // Second poll succeeds and delivers an update.
                2 -> respond("""{"ok":true,"result":[{"update_id":42}]}""", HttpStatusCode.OK, jsonHeaders)
                else -> respond("""{"ok":true,"result":[]}""", HttpStatusCode.OK, jsonHeaders)
            }
        }

        val kotbot = Kotbot(token = "test", httpClient = HttpClient(engine))

        val batches = kotbot
            .receiveUpdates(timeout = 0, retryDelay = 10.milliseconds)
            .take(1)
            .toList()

        assertEquals(1, batches.size)
        assertEquals(listOf(42), batches.single().map { it.update_id })
        assertTrue(requestCount.get() >= 2) { "flow should retry after the failed poll" }
    }

    @Test
    fun `emits a batch for every successful poll including empty ones`() = runTest {
        val requestCount = AtomicInteger(0)
        val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

        val engine = MockEngine { _ ->
            when (requestCount.incrementAndGet()) {
                // An empty poll must still surface as an emission (the liveness tick).
                1 -> respond("""{"ok":true,"result":[]}""", HttpStatusCode.OK, jsonHeaders)
                else -> respond("""{"ok":true,"result":[{"update_id":7}]}""", HttpStatusCode.OK, jsonHeaders)
            }
        }

        val kotbot = Kotbot(token = "test", httpClient = HttpClient(engine))

        val batches = kotbot
            .receiveUpdates(timeout = 0, retryDelay = 10.milliseconds)
            .take(2)
            .toList()

        assertEquals(emptyList<Int>(), batches[0].map { it.update_id })
        assertEquals(listOf(7), batches[1].map { it.update_id })
    }
}
