package io.heapy.kotbot.infra.openai

import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.ReasoningEffort
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.net.InetSocketAddress
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class GptApiTest {
    @Test
    fun `luna summaries disable reasoning and preserve the short output budget`() = runTest {
        withApi { api, requests ->
            val result = api.complete(
                model = "gpt-6-luna",
                systemPrompt = "Summarize the user's programming interests.",
                userPrompt = "I use Kotlin and Ktor.",
                temperature = 0.3,
                maxTokens = 512,
                reasoningEffort = ReasoningEffort.NONE,
            )

            assertEquals("Kotlin backend developer.", result)
            val request = requests.poll(5, TimeUnit.SECONDS)!!
            assertEquals("gpt-6-luna", request.getValue("model").jsonPrimitive.content)
            assertEquals("none", request.getValue("reasoning_effort").jsonPrimitive.content)
            assertEquals("512", request.getValue("max_completion_tokens").jsonPrimitive.content)
            assertEquals("0.3", request.getValue("temperature").jsonPrimitive.content)
        }
    }

    @Test
    fun `reasoning requests omit incompatible sampling parameters`() = runTest {
        withApi { api, requests ->
            val _ = api.complete(
                model = "gpt-6-luna",
                systemPrompt = "Summarize.",
                userPrompt = "Kotlin.",
                temperature = 0.3,
                maxTokens = 4096,
                reasoningEffort = ReasoningEffort.LOW,
            )

            val request = requests.poll(5, TimeUnit.SECONDS)!!
            assertEquals("low", request.getValue("reasoning_effort").jsonPrimitive.content)
            for (field in listOf("temperature", "top_p", "frequency_penalty", "presence_penalty")) {
                assertFalse(field in request, field)
            }
        }
    }

    @Test
    fun `public Kotlin replies use Luna without reasoning and retain their output budget`() = runTest {
        withApi { api, requests ->
            val _ = GptService(api).complete("What is a Kotlin data class?")

            val request = requests.poll(5, TimeUnit.SECONDS)!!
            assertEquals("gpt-6-luna", request.getValue("model").jsonPrimitive.content)
            assertEquals("1.01", request.getValue("temperature").jsonPrimitive.content)
            assertEquals("1024", request.getValue("max_completion_tokens").jsonPrimitive.content)
            assertEquals("none", request.getValue("reasoning_effort").jsonPrimitive.content)
        }
    }

    private suspend fun withApi(block: suspend (GptApi, LinkedBlockingQueue<JsonObject>) -> Unit) {
        val requests = LinkedBlockingQueue<JsonObject>()
        val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        val _ = server.createContext("/v1/chat/completions") { exchange ->
            exchange.use {
                requests.put(Json.parseToJsonElement(it.requestBody.bufferedReader().readText()).jsonObject)
                val response = """
                    {"id":"chatcmpl-test","object":"chat.completion","created":0,"model":"test",
                     "choices":[{"index":0,"finish_reason":"stop",
                     "message":{"role":"assistant","content":"Kotlin backend developer."}}]}
                """.trimIndent().toByteArray()
                it.responseHeaders.add("Content-Type", "application/json")
                it.sendResponseHeaders(200, response.size.toLong())
                it.responseBody.write(response)
            }
        }
        server.start()
        try {
            val client = OpenAIOkHttpClient.builder()
                .apiKey("test-key")
                .baseUrl("http://127.0.0.1:${server.address.port}/v1")
                .maxRetries(0)
                .build()
            try {
                block(GptApi(client), requests)
            } finally {
                client.close()
            }
        } finally {
            server.stop(0)
        }
    }
}
