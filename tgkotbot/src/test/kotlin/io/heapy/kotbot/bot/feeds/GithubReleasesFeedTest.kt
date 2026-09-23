package io.heapy.kotbot.bot.feeds

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class GithubReleasesFeedTest {
    private val releases = javaClass.getResource("/feeds/kotlin-github-releases.json")!!.readText()
    private val realRelease = Json.parseToJsonElement(releases).jsonArray.first().jsonObject

    private fun feed(engine: MockEngine) =
        GithubReleasesFeed(
            client = HttpClient(engine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
                install(HttpTimeout)
            },
            url = "https://api.github.com/repos/JetBrains/kotlin/releases?per_page=30",
            threadId = 293400,
        )

    private fun respondingWith(body: String) =
        MockEngine { request ->
            assertEquals(FEED_USER_AGENT, request.headers[HttpHeaders.UserAgent])
            assertEquals("application/vnd.github+json", request.headers[HttpHeaders.Accept])
            respond(
                content = body,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

    private fun realReleaseWith(vararg overrides: Pair<String, JsonElement>) =
        JsonObject(realRelease + overrides)

    @Test
    fun `parses real releases response`() = runTest {
        val items = feed(respondingWith(releases)).fetch()

        assertEquals(30, items.size)
        assertEquals(
            Json.parseToJsonElement(releases).jsonArray.map { it.jsonObject.getValue("tag_name").jsonPrimitive.content },
            items.map { it.key },
        )
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
    fun `skips drafts and tags other than stable beta and rc`() = runTest {
        val body = JsonArray(
            listOf(
                realRelease,
                realReleaseWith("tag_name" to JsonPrimitive("v2.5.0-dev-1234")),
                realReleaseWith("tag_name" to JsonPrimitive("v2.1.0-M1")),
                realReleaseWith(
                    "tag_name" to JsonPrimitive("v2.6.0"),
                    "draft" to JsonPrimitive(true),
                    "published_at" to JsonNull,
                ),
            )
        ).toString()

        val items = feed(respondingWith(body)).fetch()

        assertEquals(listOf("v2.5.0-Beta1"), items.map { it.key })
    }

    @Test
    fun `builds title from tag when release name is empty`() = runTest {
        val body = JsonArray(listOf(realReleaseWith("name" to JsonPrimitive("")))).toString()

        val items = feed(respondingWith(body)).fetch()

        assertEquals("Kotlin 2.5.0-Beta1", items.single().title)
    }

    @Test
    fun `fails on error status`() = runTest {
        val engine = MockEngine {
            respond(content = """{"message":"API rate limit exceeded"}""", status = HttpStatusCode.Forbidden)
        }

        assertThrows<Exception> {
            val _ = feed(engine).fetch()
        }
    }
}
