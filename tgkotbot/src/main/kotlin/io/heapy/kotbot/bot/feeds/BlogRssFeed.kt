package io.heapy.kotbot.bot.feeds

import io.heapy.komok.tech.logging.logger
import io.heapy.kotbot.database.enums.FeedSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import org.w3c.dom.Element
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.io.ByteArrayInputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.time.Duration

private val log = logger<BlogRssFeed>()

class BlogRssFeed(
    private val client: HttpClient,
    private val urls: List<String>,
    override val threadId: Int,
    override val pollInterval: Duration,
) : Feed {
    override val source = FeedSource.KOTLIN_NEWS

    override suspend fun fetch(since: FeedVersion?): FeedResponse {
        for (url in urls) {
            try {
                val etag = (since as? RssVersion)?.takeIf { it.url == url }?.etag
                val response = client.get(url) {
                    feedRequest(etag)
                }
                if (response.status == HttpStatusCode.NotModified) {
                    return FeedResponse.NotModified
                }
                if (response.status != HttpStatusCode.OK) {
                    log.warn("Feed {} answered {}", url, response.status)
                    continue
                }
                val items = parseRss(response.bodyAsBytes())
                if (items.isNotEmpty()) {
                    return FeedResponse.Updated(
                        items = items,
                        version = response.headers[HttpHeaders.ETag]?.let { RssVersion(url, it) },
                    )
                }
                log.warn("Feed {} has no items", url)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                log.warn("Failed to read feed {}", url, e)
            }
        }
        log.warn("No feed URL returned items: {}", urls)
        return FeedResponse.Updated(items = emptyList(), version = null)
    }
}

private data class RssVersion(
    val url: String,
    val etag: String,
) : FeedVersion

internal fun parseRss(xml: ByteArray): List<FeedItem> {
    val factory = DocumentBuilderFactory.newInstance().apply {
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        isXIncludeAware = false
        isExpandEntityReferences = false
    }
    val items = factory
        .newDocumentBuilder()
        .apply { setErrorHandler(ThrowingErrorHandler) }
        .parse(ByteArrayInputStream(xml))
        .getElementsByTagName("item")

    return (0 until items.length).mapNotNull { index ->
        parseItem(items.item(index) as Element)
    }
}

private fun parseItem(item: Element): FeedItem? {
    val title = item.childText("title") ?: return null
    val link = item.childText("link") ?: return null
    val pubDate = item.childText("pubDate") ?: return null
    val publishedAt = try {
        ZonedDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant()
    } catch (_: DateTimeParseException) {
        log.warn("Skipping feed item {} with unsupported pubDate '{}'", link, pubDate)
        return null
    }
    return FeedItem(
        key = item.childText("guid") ?: link,
        title = title,
        url = link,
        publishedAt = publishedAt,
    )
}

/**
 * Without a handler the JDK parser prints every error to stderr, outside logback.
 */
private object ThrowingErrorHandler : ErrorHandler {
    override fun warning(exception: SAXParseException) = Unit
    override fun error(exception: SAXParseException) = throw exception
    override fun fatalError(exception: SAXParseException) = throw exception
}

private fun Element.childText(tagName: String): String? =
    getElementsByTagName(tagName)
        .item(0)
        ?.textContent
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
