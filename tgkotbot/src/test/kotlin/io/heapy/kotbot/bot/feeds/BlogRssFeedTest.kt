package io.heapy.kotbot.bot.feeds

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Instant
import kotlin.time.Duration.Companion.minutes

class BlogRssFeedTest {
    private val rss = javaClass.getResource("/feeds/kotlin-blog-rss.xml")!!.readBytes()

    private fun feed(
        urls: List<String>,
        engine: MockEngine,
    ) = BlogRssFeed(
        client = HttpClient(engine) {
            install(HttpTimeout)
        },
        urls = urls,
        threadId = 293499,
        pollInterval = 15.minutes,
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
            """.trimIndent().toByteArray()
        )

        assertEquals("https://blog.jetbrains.com/kotlin/post/", items.single().key)
    }

    @Test
    fun `skips item with unsupported pubDate and keeps the rest`() {
        val items = parseRss(
            """
            <rss version="2.0"><channel>
              <item>
                <title>Bad date</title>
                <link>https://blog.jetbrains.com/kotlin/bad/</link>
                <pubDate>Fri, 14 Aug 2026 12:15:09 UT</pubDate>
              </item>
              <item>
                <title>Good date</title>
                <link>https://blog.jetbrains.com/kotlin/good/</link>
                <pubDate>Mon, 07 Sep 2026 11:30:57 +0000</pubDate>
              </item>
            </channel></rss>
            """.trimIndent().toByteArray()
        )

        assertEquals(listOf("https://blog.jetbrains.com/kotlin/good/"), items.map { it.url })
    }

    @Test
    fun `parses feed that starts with byte order mark`() {
        assertEquals(12, parseRss(BYTE_ORDER_MARK + rss).size)
    }

    @Test
    fun `does not print parser errors to stderr`() {
        val stderr = ByteArrayOutputStream()
        val original = System.err
        System.setErr(PrintStream(stderr, true))
        try {
            assertThrows<Exception> {
                val _ = parseRss("<html>not a feed".toByteArray())
            }
        } finally {
            System.setErr(original)
        }

        assertEquals("", stderr.toString())
    }

    @Test
    fun `rejects documents with doctype`() {
        assertThrows<Exception> {
            val _ = parseRss(
                """
                <?xml version="1.0"?>
                <!DOCTYPE rss [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
                <rss version="2.0"><channel><item><title>&xxe;</title></item></channel></rss>
                """.trimIndent().toByteArray()
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

        val response = feed(urls = listOf(BLOG, MIRROR), engine = engine).fetch(since = null).updated()

        assertEquals(listOf(BLOG, MIRROR), requested)
        assertEquals(12, response.items.size)
        assertEquals(null, response.version)
    }

    @Test
    fun `answers not modified for version from previous response`() = runTest {
        val requests = mutableListOf<Pair<String, String?>>()
        val engine = MockEngine { request ->
            requests += request.url.toString() to request.headers[HttpHeaders.IfNoneMatch]
            when (requests.size) {
                1 -> respond(content = rss, status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ETag, "\"blog\""))
                else -> respond(content = "", status = HttpStatusCode.NotModified)
            }
        }
        val feed = feed(urls = listOf(BLOG, MIRROR), engine = engine)

        val first = feed.fetch(since = null).updated()
        val second = feed.fetch(since = first.version)

        assertEquals(listOf(BLOG to null, BLOG to "\"blog\""), requests)
        assertEquals(12, first.items.size)
        assertEquals(FeedResponse.NotModified, second)
    }

    @Test
    fun `does not send blog etag to mirror`() = runTest {
        val requests = mutableListOf<Pair<String, String?>>()
        val engine = MockEngine { request ->
            requests += request.url.toString() to request.headers[HttpHeaders.IfNoneMatch]
            when {
                requests.size == 1 -> respond(content = rss, status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ETag, "\"blog\""))
                request.url.host == "blog.jetbrains.com" -> respond(content = "", status = HttpStatusCode.Accepted)
                else -> respond(content = rss, status = HttpStatusCode.OK)
            }
        }
        val feed = feed(urls = listOf(BLOG, MIRROR), engine = engine)

        val first = feed.fetch(since = null).updated()
        val second = feed.fetch(since = first.version).updated()

        assertEquals(listOf(BLOG to null, BLOG to "\"blog\"", MIRROR to null), requests)
        assertEquals(12, second.items.size)
        assertEquals(null, second.version)
    }

    @Test
    fun `reads feed body with byte order mark`() = runTest {
        val engine = MockEngine {
            respond(content = BYTE_ORDER_MARK + rss, status = HttpStatusCode.OK)
        }

        val response = feed(urls = listOf(BLOG), engine = engine).fetch(since = null).updated()

        assertEquals(12, response.items.size)
    }

    @Test
    fun `returns empty update when every url fails`() = runTest {
        val engine = MockEngine { request ->
            when (request.url.host) {
                "blog.jetbrains.com" -> respond(content = "<html>not a feed", status = HttpStatusCode.OK)
                else -> respond(content = "", status = HttpStatusCode.ServiceUnavailable)
            }
        }

        val response = feed(urls = listOf(BLOG, MIRROR), engine = engine).fetch(since = null)

        assertEquals(FeedResponse.Updated(items = emptyList(), version = null), response)
    }

    private fun FeedResponse.updated() = this as FeedResponse.Updated

    private companion object {
        private const val BLOG = "https://blog.jetbrains.com/kotlin/feed/"
        private const val MIRROR = "https://feeds.feedburner.com/kotlin"
        private val BYTE_ORDER_MARK = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
    }
}
