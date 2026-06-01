package io.heapy.kotbot.bot

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * These tests pin down how the CIO client's request timeout behaves, which is what
 * [Kotbot]'s long polling ([receiveUpdates] holds `getUpdates` open for up to 50s)
 * relies on. They use a real CIO engine against a local socket that delays its
 * response, because the request-timeout semantics live in the engine and the
 * `HttpTimeout` plugin — `MockEngine` does not reproduce them.
 *
 * Key expectation under test: installing the [HttpTimeout] plugin disables the CIO
 * engine's own [requestTimeout][io.ktor.client.engine.cio.CIOEngineConfig.requestTimeout],
 * so the bare `engine { requestTimeout = ... }` line is a no-op once the plugin is present.
 */
class HttpClientTimeoutTest {
    @Test
    fun `engine requestTimeout fires and reports unknown when HttpTimeout plugin is absent`() {
        SlowServer(responseDelay = 2.seconds).use { server ->
            HttpClient(CIO) {
                engine { requestTimeout = 300L }
            }.use { client ->
                val exception = assertThrows<HttpRequestTimeoutException> {
                    runBlocking { client.get(server) }
                }

                // Without the plugin there is no HttpTimeoutCapability on the request,
                // so the exception cannot name the timeout -> "unknown ms".
                // This is exactly the message seen in production: request_timeout=unknown ms.
                assertTrue("request_timeout=unknown ms" in exception.message.orEmpty()) {
                    "expected an unknown request timeout, was: ${exception.message}"
                }
            }
        }
    }

    @Test
    fun `installing HttpTimeout makes engine requestTimeout a no-op`() {
        // The server answers after 1s. If engine.requestTimeout (300ms) were still active
        // the call would time out; it succeeding proves the engine value is ignored once
        // the HttpTimeout plugin is installed.
        SlowServer(responseDelay = 1.seconds).use { server ->
            HttpClient(CIO) {
                engine {
                    requestTimeout = 300L
                }
                install(HttpTimeout) {
                    socketTimeoutMillis = 5_000
                }
            }.use { client ->
                val response = runBlocking { client.get(server) }

                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    @Test
    fun `HttpTimeout requestTimeoutMillis governs the request and reports its value`() {
        // engine.requestTimeout is generous (10s) and the server answers in 2s, so if the
        // engine value were in charge the call would succeed. It instead times out at 300ms
        // and names the value -> proves the plugin's requestTimeoutMillis is what governs.
        SlowServer(responseDelay = 2.seconds).use { server ->
            HttpClient(CIO) {
                engine {
                    requestTimeout = 10_000
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 300L
                }
            }.use { client ->
                val exception = assertThrows<HttpRequestTimeoutException> {
                    runBlocking { client.get(server) }
                }

                assertTrue("request_timeout=${300L} ms" in exception.message.orEmpty()) {
                    "expected the configured timeout to be reported, was: ${exception.message}"
                }
            }
        }
    }

    @Test
    fun `socketTimeout fires when the server is idle longer than the socket timeout`() {
        // Long polling keeps the socket idle for up to the poll timeout, so socketTimeoutMillis
        // must always exceed it. Here the idle period (2s) exceeds the socket timeout (300ms).
        SlowServer(responseDelay = 2.seconds).use { server ->
            HttpClient(CIO) {
                install(HttpTimeout) {
                    socketTimeoutMillis = 300L
                }
            }.use { client ->
                assertThrows<SocketTimeoutException> {
                    runBlocking { client.get(server) }
                }
            }
        }
    }

    @Test
    fun `a long poll within the configured timeouts completes successfully`() {
        // Mirrors the shipped config relationship: both timeouts comfortably exceed the
        // response delay (a stand-in for a long poll that returns just before its deadline).
        SlowServer(responseDelay = 800.milliseconds).use { server ->
            HttpClient(CIO) {
                install(HttpTimeout) {
                    requestTimeoutMillis = 3_000
                    socketTimeoutMillis = 3_000
                }
            }.use { client ->
                val response = runBlocking { client.get(server) }

                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    private suspend fun HttpClient.get(server: SlowServer): HttpResponse =
        post("http://localhost:${server.port}/")

    /**
     * A bare TCP server that accepts a connection, waits [responseDelay] without sending
     * anything, then writes a minimal `200 OK` JSON response and closes the connection.
     * The silent wait is what lets each test drive a specific timeout deterministically.
     */
    private class SlowServer(
        private val responseDelay: Duration,
    ) : AutoCloseable {
        private val serverSocket = ServerSocket(0)

        val port: Int get() = serverSocket.localPort

        init {
            thread(isDaemon = true, name = "slow-server") {
                while (!serverSocket.isClosed) {
                    val socket = try {
                        serverSocket.accept()
                    } catch (_: Throwable) {
                        break
                    }
                    thread(isDaemon = true) { respond(socket) }
                }
            }
        }

        private fun respond(socket: Socket) {
            socket.use {
                try {
                    // Drain the request first: closing a socket with unread bytes makes the OS
                    // send a TCP RST, which would discard the response we are about to write.
                    drainRequest(socket.getInputStream())
                    Thread.sleep(responseDelay.inWholeMilliseconds)
                    val body = """{"ok":true,"result":[]}""".toByteArray()
                    val head = buildString {
                        append("HTTP/1.1 200 OK\r\n")
                        append("Content-Type: application/json\r\n")
                        append("Content-Length: ${body.size}\r\n")
                        append("Connection: close\r\n")
                        append("\r\n")
                    }
                    socket.getOutputStream().apply {
                        write(head.toByteArray())
                        write(body)
                        flush()
                    }
                } catch (_: Throwable) {
                    // The client aborted after its timeout fired — expected for the timeout tests.
                }
            }
        }

        private fun drainRequest(input: InputStream) {
            val header = StringBuilder()
            while (!header.endsWith("\r\n\r\n")) {
                val b = input.read()
                if (b == -1) return
                header.append(b.toChar())
            }
            val contentLength = Regex("(?i)content-length:\\s*(\\d+)")
                .find(header)
                ?.groupValues?.get(1)
                ?.toInt()
                ?: 0
            repeat(contentLength) {
                if (input.read() == -1) return
            }
        }

        override fun close() {
            serverSocket.close()
        }
    }
}
