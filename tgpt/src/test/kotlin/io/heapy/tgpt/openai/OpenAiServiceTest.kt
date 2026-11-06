package io.heapy.tgpt.openai

import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.responses.ResponseCreateParams
import com.openai.models.responses.WebSearchTool
import com.sun.net.httpserver.HttpServer
import com.typesafe.config.ConfigFactory
import io.heapy.tgpt.infra.configuration.OpenAiConfiguration
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.net.InetSocketAddress
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class OpenAiServiceTest {
    @Test
    fun `Sol requests use explicit reasoning and retain web search and output limits`() = runTest {
        val configuration = OpenAiConfiguration(apiKey = "test-key")
        assertEquals("gpt-6.1-sol", configuration.model)
        assertEquals(configuration.model, ConfigFactory.parseResources("reference.conf").getString("openai.model"))

        val requests = LinkedBlockingQueue<JsonObject>()
        val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        val _ = server.createContext("/v1/responses") { exchange ->
            exchange.use {
                requests.put(Json.parseToJsonElement(it.requestBody.bufferedReader().readText()).jsonObject)
                val response = """
                    {"id":"resp-test","object":"response","created_at":0,"status":"completed",
                     "model":"gpt-6.1-sol","output":[],"parallel_tool_calls":true,
                     "tool_choice":"auto","tools":[]}
                """.trimIndent().toByteArray()
                it.responseHeaders.add("Content-Type", "application/json")
                it.sendResponseHeaders(200, response.size.toLong())
                it.responseBody.write(response)
            }
        }
        server.start()
        try {
            val client = OpenAIOkHttpClient.builder()
                .apiKey(configuration.apiKey)
                .baseUrl("http://127.0.0.1:${server.address.port}/v1")
                .maxRetries(0)
                .build()
            try {
                val service = OpenAiService(client, configuration)
                val response = service.createResponse(
                    ResponseCreateParams.builder()
                        .model(service.model())
                        .input("Explain Kotlin val and var.")
                        .maxOutputTokens(service.maxTokens().toLong())
                        .addTool(WebSearchTool.builder().type(WebSearchTool.Type.WEB_SEARCH).build())
                        .build()
                )

                assertEquals("resp-test", response.id())
                val request = requests.poll(5, TimeUnit.SECONDS)!!
                assertEquals("gpt-6.1-sol", request.getValue("model").jsonPrimitive.content)
                assertEquals("medium", request.getValue("reasoning").jsonObject.getValue("effort").jsonPrimitive.content)
                assertEquals("4096", request.getValue("max_output_tokens").jsonPrimitive.content)
                assertEquals("web_search", request.getValue("tools").jsonArray.single().jsonObject.getValue("type").jsonPrimitive.content)
                assertFalse("temperature" in request)
                assertFalse("top_p" in request)
            } finally {
                client.close()
            }
        } finally {
            server.stop(0)
        }
    }
}
