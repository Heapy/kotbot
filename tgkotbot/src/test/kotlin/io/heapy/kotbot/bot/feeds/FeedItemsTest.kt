package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.infra.markdown.createMarkdownModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class FeedItemsTest {
    private val markdown = createMarkdownModule {}.markdown

    private fun item(
        key: String,
        publishedAt: String = "2026-09-07T10:31:56Z",
        title: String = "Kotlin ${key.removePrefix("v")}",
    ) = FeedItem(
        key = key,
        title = title,
        url = "https://github.com/JetBrains/kotlin/releases/tag/$key",
        publishedAt = Instant.parse(publishedAt),
    )

    @Test
    fun `selects unknown items oldest first`() {
        val beta = item("v2.5.0-Beta1", publishedAt = "2026-09-23T12:02:11Z")
        val stable = item("v2.4.20", publishedAt = "2026-09-07T10:31:56Z")
        val rc = item("v2.4.20-RC3", publishedAt = "2026-09-02T09:49:47Z")

        val selected = selectNewItems(
            fetched = listOf(beta, stable, rc, beta),
            knownKeys = setOf("v2.4.20"),
        )

        assertEquals(listOf(rc, beta), selected)
    }

    @Test
    fun `formats bold title link`() {
        assertEquals(
            "*[Kotlin 2\\.4\\.20](https://github.com/JetBrains/kotlin/releases/tag/v2.4.20)*",
            markdown.formatFeedItem(item("v2.4.20")),
        )
    }

    @Test
    fun `keeps markdown characters in title literal`() {
        assertEquals(
            "*[a\\*b\\_c \\[d\\] \\`e\\` \\(f\\) \\#1\\!](https://github.com/JetBrains/kotlin/releases/tag/v1)*",
            markdown.formatFeedItem(item("v1", title = "a*b_c [d] `e` (f) #1!")),
        )
    }
}
