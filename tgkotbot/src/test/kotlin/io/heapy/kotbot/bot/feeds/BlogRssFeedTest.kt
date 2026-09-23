package io.heapy.kotbot.bot.feeds

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class BlogRssFeedTest {
    private val rss = javaClass.getResource("/feeds/kotlin-blog-rss.xml")!!.readText()

    private fun feed(
        urls: List<String>,
        engine: MockEngine,
    ) = BlogRssFeed(
        client = HttpClient(engine) {
            install(HttpTimeout)
        },
        urls = urls,
        threadId = 293499,
    )

    @Test
    fun `parses real feed items`() {
        val items = parseRss(rss)

        assertEquals(12, items.size)
        assertEquals(
            FeedItem(
                key = "https://blog.jetbrains.com/?post_type=kotlin&p=737362",
                title = "Kotlin 2.4.20 Released",
                url = "https://blog.jetbrains.com/kotlin/2026/09/kotlin-2-4-20-released/",
                publishedAt = Instant.parse("2026-09-07T11:30:57Z"),
            ),
            items.first(),
        )
        assertTrue(items.any { it.title.startsWith("Kodee’s Kotlin Roundup") })
    }

    @Test
    fun `falls back to guid-less link as key`() {
        val items = parseRss(
            """
            <rss version="2.0"><channel>
              <item>
                <title>Post</title>
                <link>https://blog.jetbrains.com/kotlin/post/</link>
                <pubDate>Mon, 07 Sep 2026 11:30:57 +0000</pubDate>
              </item>
            </channel></rss>
            """.trimIndent()
        )

        assertEquals("https://blog.jetbrains.com/kotlin/post/", items.single().key)
    }

    @Test
    fun `rejects documents with doctype`() {
        assertThrows<Exception> {
            val _ = parseRss(
                """
                <?xml version="1.0"?>
                <!DOCTYPE rss [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
                <rss version="2.0"><channel><item><title>&xxe;</title></item></channel></rss>
                """.trimIndent()
            )
        }
    }

    @Test
    fun `uses next url when first answers with waf challenge`() = runTest {
        val requested = mutableListOf<String>()
        val engine = MockEngine { request ->
            requested += request.url.toString()
            assertEquals(FEED_USER_AGENT, request.headers[HttpHeaders.UserAgent])
            when (request.url.host) {
                "blog.jetbrains.com" -> respond(content = "", status = HttpStatusCode.Accepted)
                else -> respond(content = rss, status = HttpStatusCode.OK)
            }
        }

        val items = feed(
            urls = listOf("https://blog.jetbrains.com/kotlin/feed/", "https://feeds.feedburner.com/kotlin"),
            engine = engine,
        ).fetch()

        assertEquals(12, items.size)
        assertEquals(
            listOf("https://blog.jetbrains.com/kotlin/feed/", "https://feeds.feedburner.com/kotlin"),
            requested,
        )
    }

    @Test
    fun `returns empty list when every url fails`() = runTest {
        val engine = MockEngine { request ->
            when (request.url.host) {
                "blog.jetbrains.com" -> respond(content = "<html>not a feed", status = HttpStatusCode.OK)
                else -> respond(content = "", status = HttpStatusCode.ServiceUnavailable)
            }
        }

        val items = feed(
            urls = listOf("https://blog.jetbrains.com/kotlin/feed/", "https://feeds.feedburner.com/kotlin"),
            engine = engine,
        ).fetch()

        assertEquals(emptyList<FeedItem>(), items)
    }
}
