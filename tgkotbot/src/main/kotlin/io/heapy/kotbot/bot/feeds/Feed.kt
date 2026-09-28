package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.database.enums.FeedSource
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import java.time.Instant
import kotlin.time.Duration

data class FeedItem(
    val key: String,
    val title: String,
    val url: String,
    val publishedAt: Instant,
)

interface Feed {
    val source: FeedSource
    val threadId: Int
    val pollInterval: Duration

    suspend fun fetch(since: FeedVersion?): FeedResponse
}

/**
 * Opaque to callers: each feed keeps its own private implementation.
 */
sealed interface FeedVersion

sealed interface FeedResponse {
    data object NotModified : FeedResponse

    data class Updated(
        val items: List<FeedItem>,
        val version: FeedVersion?,
    ) : FeedResponse
}

internal fun selectNewItems(
    fetched: List<FeedItem>,
    knownKeys: Set<String>,
): List<FeedItem> =
    fetched
        .filterNot { it.key in knownKeys }
        .distinctBy { it.key }
        .sortedBy { it.publishedAt }

internal fun HttpRequestBuilder.feedRequest(etag: String?) {
    header(HttpHeaders.UserAgent, FEED_USER_AGENT)
    if (etag != null) {
        header(HttpHeaders.IfNoneMatch, etag)
    }
    timeout {
        requestTimeoutMillis = FEED_REQUEST_TIMEOUT_MS
    }
}

internal const val FEED_USER_AGENT = "kotbot (+https://github.com/Heapy/kotbot)"
private const val FEED_REQUEST_TIMEOUT_MS = 30_000L
