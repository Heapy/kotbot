package io.heapy.kotbot.bot.feeds

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.time.Duration.Companion.minutes

class GithubReleasesFeedTest {
    private val response = javaClass.getResource("/feeds/kotlin-github-releases-graphql.json")!!.readText()
    private val realNodes = Json.parseToJsonElement(response)
        .jsonObject.getValue("data")
        .jsonObject.getValue("repository")
        .jsonObject.getValue("releases")
        .jsonObject.getValue("nodes")
        .jsonArray
    private val realRelease = realNodes.first().jsonObject

    private fun feed(
        engine: MockEngine,
        token: String? = "test-token",
    ) = GithubReleasesFeed(
        client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpTimeout)
        },
        token = token,
        threadId = 293400,
        pollInterval = 5.minutes,
    )

    private fun respondingWith(body: String) =
        MockEngine {
            respond(
                content = body,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

    private fun responseWith(vararg nodes: JsonElement) =
        """{"data":{"repository":{"releases":{"nodes":${JsonArray(nodes.toList())}}}}}"""

    private fun realReleaseWith(vararg overrides: Pair<String, JsonElement>) =
        JsonObject(realRelease + overrides)

    private suspend fun GithubReleasesFeed.items() =
        (fetch(since = null) as FeedResponse.Updated).items

    @Test
    fun `parses real graphql response`() = runTest {
        val result = feed(respondingWith(response)).fetch(since = null) as FeedResponse.Updated
        val items = result.items

        assertEquals(null, result.version)
        assertEquals(
            realNodes.map { it.jsonObject.getValue("tagName").jsonPrimitive.content },
            items.map { it.key },
        )
        assertEquals(10, items.size)
        assertEquals(
            FeedItem(
                key = "v2.5.0-Beta1",
                title = "Kotlin 2.5.0-Beta1",
                url = "https://github.com/JetBrains/kotlin/releases/tag/v2.5.0-Beta1",
                publishedAt = Instant.parse("2026-09-23T12:02:11Z"),
            ),
            items.first(),
        )
    }

    @Test
    fun `posts releases query with bearer token`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(HttpMethod.Post, request.method)
            assertEquals("https://api.github.com/graphql", request.url.toString())
            assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
            assertEquals(FEED_USER_AGENT, request.headers[HttpHeaders.UserAgent])
            val query = Json.parseToJsonElement((request.body as TextContent).text)
                .jsonObject.getValue("query").jsonPrimitive.content
            assertTrue(query.contains("""repository(owner: "JetBrains", name: "kotlin")"""), query)
            assertTrue(query.contains("releases(first: 10"), query)
            respond(
                content = response,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        assertEquals(10, feed(engine).items().size)
    }

    @Test
    fun `skips drafts and tags other than stable beta and rc`() = runTest {
        val body = responseWith(
            realRelease,
            realReleaseWith("tagName" to JsonPrimitive("v2.5.0-dev-1234")),
            realReleaseWith("tagName" to JsonPrimitive("build-2.5.0-dev-8624")),
            realReleaseWith("tagName" to JsonPrimitive("v2.1.0-M1")),
            realReleaseWith(
                "tagName" to JsonPrimitive("v2.6.0"),
                "isDraft" to JsonPrimitive(true),
                "publishedAt" to JsonPrimitive(null as String?),
            ),
        )

        val items = feed(respondingWith(body)).items()

        assertEquals(listOf("v2.5.0-Beta1"), items.map { it.key })
    }

    @Test
    fun `builds title from tag when release name is empty`() = runTest {
        val body = responseWith(realReleaseWith("name" to JsonPrimitive("")))

        val items = feed(respondingWith(body)).items()

        assertEquals("Kotlin 2.5.0-Beta1", items.single().title)
    }

    @Test
    fun `fails on graphql errors`() = runTest {
        val body = """{"errors":[{"type":"RATE_LIMITED","message":"API rate limit exceeded"}]}"""

        val error = assertThrows<IllegalStateException> {
            val _ = feed(respondingWith(body)).fetch(since = null)
        }
        assertEquals("GitHub GraphQL errors: [API rate limit exceeded]", error.message)
    }

    @Test
    fun `fails with status on error response`() = runTest {
        val engine = MockEngine {
            respond(
                content = """{"message":"Bad credentials"}""",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val error = assertThrows<IllegalStateException> {
            val _ = feed(engine).fetch(since = null)
        }
        assertEquals("GitHub GraphQL answered 401 Unauthorized", error.message)
    }

    @Test
    fun `fails without request when token is missing`() = runTest {
        var requests = 0
        val engine = MockEngine {
            requests++
            respond(content = response, status = HttpStatusCode.OK)
        }

        val error = assertThrows<IllegalStateException> {
            val _ = feed(engine, token = null).fetch(since = null)
        }
        assertEquals("KOTBOT_GITHUB_TOKEN is not set", error.message)
        assertEquals(0, requests)
    }
}
