package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.database.enums.FeedSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import java.time.Instant
import kotlin.time.Duration

class GithubReleasesFeed(
    private val client: HttpClient,
    private val token: String?,
    override val threadId: Int,
    override val pollInterval: Duration,
) : Feed {
    override val source = FeedSource.KOTLIN_RELEASES

    override suspend fun fetch(since: FeedVersion?): FeedResponse {
        val token = checkNotNull(token) { "KOTBOT_GITHUB_TOKEN is not set" }
        val response = client.post(GITHUB_GRAPHQL_URL) {
            feedRequest(etag = null)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(GraphqlRequest(RELEASES_QUERY))
        }
        check(response.status == HttpStatusCode.OK) { "GitHub GraphQL answered ${response.status}" }

        val body = response.body<GraphqlResponse>()
        check(body.errors.isEmpty()) { "GitHub GraphQL errors: ${body.errors.map { it.message }}" }
        val releases = checkNotNull(body.data?.repository) { "GitHub GraphQL returned no repository" }
            .releases
            .nodes

        val items = releases.mapNotNull { release ->
            val publishedAt = release.publishedAt
            if (release.isDraft || publishedAt == null || !RELEASE_TAG.matches(release.tagName)) {
                return@mapNotNull null
            }
            FeedItem(
                key = release.tagName,
                title = release.name?.takeIf { it.isNotBlank() }
                    ?: "Kotlin ${release.tagName.removePrefix("v")}",
                url = release.url,
                publishedAt = Instant.parse(publishedAt),
            )
        }
        return FeedResponse.Updated(items = items, version = null)
    }

    @Serializable
    private data class GraphqlRequest(
        val query: String,
    )

    @Serializable
    private data class GraphqlResponse(
        val data: Data? = null,
        val errors: List<GraphqlError> = emptyList(),
    )

    @Serializable
    private data class GraphqlError(
        val message: String,
    )

    @Serializable
    private data class Data(
        val repository: Repository? = null,
    )

    @Serializable
    private data class Repository(
        val releases: Releases,
    )

    @Serializable
    private data class Releases(
        val nodes: List<Release>,
    )

    @Serializable
    private data class Release(
        val tagName: String,
        val name: String? = null,
        val url: String,
        val isDraft: Boolean = false,
        val publishedAt: String? = null,
    )

    private companion object {
        private const val GITHUB_GRAPHQL_URL = "https://api.github.com/graphql"
        private const val RELEASES_QUERY =
            """query { repository(owner: "JetBrains", name: "kotlin") { releases(first: 10, orderBy: {field: CREATED_AT, direction: DESC}) { nodes { tagName name url isDraft publishedAt } } } }"""
        private val RELEASE_TAG = Regex("""v\d+\.\d+\.\d+(-(Beta|RC)\d*)?""")
    }
}
