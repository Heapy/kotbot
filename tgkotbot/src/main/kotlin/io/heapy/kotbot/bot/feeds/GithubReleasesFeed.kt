package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.database.enums.FeedSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

class GithubReleasesFeed(
    private val client: HttpClient,
    private val url: String,
    override val threadId: Int,
) : Feed {
    override val source = FeedSource.KOTLIN_RELEASES

    override suspend fun fetch(): List<FeedItem> =
        client
            .get(url) {
                expectSuccess = true
                header(HttpHeaders.Accept, "application/vnd.github+json")
                header(HttpHeaders.UserAgent, FEED_USER_AGENT)
                timeout {
                    requestTimeoutMillis = FEED_REQUEST_TIMEOUT_MS
                }
            }
            .body<List<GithubRelease>>()
            .mapNotNull { release ->
                val publishedAt = release.publishedAt
                if (release.draft || publishedAt == null || !RELEASE_TAG.matches(release.tagName)) {
                    return@mapNotNull null
                }
                FeedItem(
                    key = release.tagName,
                    title = release.name?.takeIf { it.isNotBlank() }
                        ?: "Kotlin ${release.tagName.removePrefix("v")}",
                    url = release.htmlUrl,
                    publishedAt = Instant.parse(publishedAt),
                )
            }

    @Serializable
    private data class GithubRelease(
        @SerialName("tag_name")
        val tagName: String,
        val name: String? = null,
        @SerialName("html_url")
        val htmlUrl: String,
        val draft: Boolean = false,
        @SerialName("published_at")
        val publishedAt: String? = null,
    )

    private companion object {
        private val RELEASE_TAG = Regex("""v\d+\.\d+\.\d+(-(Beta|RC)\d*)?""")
    }
}
