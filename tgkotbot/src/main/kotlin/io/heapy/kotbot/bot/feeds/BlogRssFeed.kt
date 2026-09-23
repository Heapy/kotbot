package io.heapy.kotbot.bot.feeds

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.database.enums.FeedSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

class BlogRssFeed(
    private val client: HttpClient,
    private val urls: List<String>,
    override val threadId: Int,
) : Feed {
    override val source = FeedSource.KOTLIN_NEWS

    override suspend fun fetch(): List<FeedItem> {
        for (url in urls) {
            try {
                val response = client.get(url) {
                    header(HttpHeaders.UserAgent, FEED_USER_AGENT)
                    timeout {
                        requestTimeoutMillis = FEED_REQUEST_TIMEOUT_MS
                    }
                }
                if (response.status != HttpStatusCode.OK) {
                    log.warn("Feed {} answered {}", url, response.status)
                    continue
                }
                val items = parseRss(response.bodyAsText())
                if (items.isNotEmpty()) {
                    return items
                }
                log.warn("Feed {} has no items", url)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                log.warn("Failed to read feed {}", url, e)
            }
        }
        log.warn("No feed URL returned items: {}", urls)
        return emptyList()
    }

    private companion object : Logger()
}

internal fun parseRss(xml: String): List<FeedItem> {
    val factory = DocumentBuilderFactory.newInstance().apply {
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        isXIncludeAware = false
        isExpandEntityReferences = false
    }
    val items = factory
        .newDocumentBuilder()
        .parse(InputSource(StringReader(xml)))
        .getElementsByTagName("item")

    return (0 until items.length).mapNotNull { index ->
        val item = items.item(index) as Element
        val title = item.childText("title") ?: return@mapNotNull null
        val link = item.childText("link") ?: return@mapNotNull null
        val pubDate = item.childText("pubDate") ?: return@mapNotNull null
        FeedItem(
            key = item.childText("guid") ?: link,
            title = title,
            url = link,
            publishedAt = ZonedDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant(),
        )
    }
}

private fun Element.childText(tagName: String): String? =
    getElementsByTagName(tagName)
        .item(0)
        ?.textContent
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
