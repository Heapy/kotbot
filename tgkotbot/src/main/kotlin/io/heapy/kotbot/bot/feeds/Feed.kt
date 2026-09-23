package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.database.enums.FeedSource
import java.time.Instant

data class FeedItem(
    val key: String,
    val title: String,
    val url: String,
    val publishedAt: Instant,
)

interface Feed {
    val source: FeedSource
    val threadId: Int

    suspend fun fetch(): List<FeedItem>
}

internal fun selectNewItems(
    fetched: List<FeedItem>,
    knownKeys: Set<String>,
): List<FeedItem> =
    fetched
        .filterNot { it.key in knownKeys }
        .distinctBy { it.key }
        .sortedBy { it.publishedAt }

internal const val FEED_USER_AGENT = "kotbot (+https://github.com/Heapy/kotbot)"
internal const val FEED_REQUEST_TIMEOUT_MS = 30_000L
