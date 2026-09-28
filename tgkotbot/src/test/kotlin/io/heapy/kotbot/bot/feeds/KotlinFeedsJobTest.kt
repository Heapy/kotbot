package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.WithMockTransaction
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.database.enums.FeedSource
import io.heapy.kotbot.infra.jdbc.MockTransactionContext
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.heapy.kotbot.infra.markdown.createMarkdownModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.IOException
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@WithMockTransaction
class KotlinFeedsJobTest {
    private data class SentMessage(
        val chatId: Long,
        val threadId: Int,
        val text: String,
        val parseMode: String?,
    )

    private sealed interface TelegramReply {
        data object Ok : TelegramReply

        data class Error(
            val code: Int,
            val description: String,
            val retryAfter: Int? = null,
        ) : TelegramReply

        data object ConnectionReset : TelegramReply
    }

    private val requests = mutableListOf<SentMessage>()
    private val posted = mutableListOf<SentMessage>()

    private fun kotbot(reply: (SentMessage) -> TelegramReply = { TelegramReply.Ok }) =
        Kotbot(
            token = "test",
            httpClient = HttpClient(MockEngine { request ->
                val body = Json.parseToJsonElement((request.body as TextContent).text).jsonObject
                val message = SentMessage(
                    chatId = body.getValue("chat_id").jsonPrimitive.long,
                    threadId = body.getValue("message_thread_id").jsonPrimitive.int,
                    text = body.getValue("text").jsonPrimitive.content,
                    parseMode = body["parse_mode"]?.jsonPrimitive?.contentOrNull,
                )
                requests += message
                when (val answer = reply(message)) {
                    TelegramReply.Ok -> {
                        posted += message
                        respond(
                            content = """{"ok":true,"result":{"message_id":${posted.size},"date":0,"chat":{"id":$CHAT_ID,"type":"supergroup"}}}""",
                            status = HttpStatusCode.OK,
                        )
                    }
                    is TelegramReply.Error -> respond(
                        content = """{"ok":false,"error_code":${answer.code},"description":"${answer.description}"""" +
                            (answer.retryAfter?.let { ""","parameters":{"retry_after":$it}""" } ?: "") +
                            "}",
                        status = HttpStatusCode.fromValue(answer.code),
                    )
                    TelegramReply.ConnectionReset -> throw IOException("connection reset")
                }
            }),
        )

    private val transactionProvider = mockk<TransactionProvider> {
        coEvery { transaction<Any?>(any()) } coAnswers {
            firstArg<suspend TransactionContext.() -> Any?>().invoke(MockTransactionContext)
        }
    }

    private fun feed(
        source: FeedSource,
        threadId: Int,
        pollInterval: Duration = 15.minutes,
        fetch: () -> List<FeedItem>,
    ) = object : Feed {
        override val source = source
        override val threadId = threadId
        override val pollInterval = pollInterval
        override suspend fun fetch(since: FeedVersion?) = FeedResponse.Updated(items = fetch(), version = null)
    }

    private val rss = javaClass.getResource("/feeds/kotlin-blog-rss.xml")!!.readText()

    private fun blogFeed(ifNoneMatch: MutableList<String?> = mutableListOf()) =
        BlogRssFeed(
            client = HttpClient(MockEngine { request ->
                ifNoneMatch += request.headers[HttpHeaders.IfNoneMatch]
                when (request.headers[HttpHeaders.IfNoneMatch]) {
                    null -> respond(content = rss, status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ETag, "\"blog\""))
                    else -> respond(content = "", status = HttpStatusCode.NotModified)
                }
            }) {
                install(HttpTimeout)
            },
            urls = listOf("https://blog.jetbrains.com/kotlin/feed/"),
            threadId = NEWS_THREAD,
            pollInterval = 15.minutes,
        )

    context(_: MockTransactionContext)
    private fun daoKnowingAllBlogItemsButNewest() =
        mockk<FeedItemDao>(relaxed = true) {
            coEvery { findKnownKeys(any(), any()) } returns parseRss(rss).drop(1).map { it.key }.toSet()
        }

    private fun TestScope.job(
        feeds: List<Feed>,
        dao: FeedItemDao,
        kotbot: Kotbot = kotbot(),
        enabled: Boolean = true,
    ) = KotlinFeedsJob(
        enabled = enabled,
        feeds = feeds,
        feedItemDao = dao,
        kotbot = kotbot,
        markdown = createMarkdownModule {}.markdown,
        transactionProvider = transactionProvider,
        applicationScope = backgroundScope,
        chatId = CHAT_ID,
        sendInterval = 3.seconds,
    )

    private val stable = FeedItem(
        key = "v2.4.20",
        title = "Kotlin 2.4.20",
        url = "https://github.com/JetBrains/kotlin/releases/tag/v2.4.20",
        publishedAt = Instant.parse("2026-09-07T10:31:56Z"),
    )
    private val beta = FeedItem(
        key = "v2.5.0-Beta1",
        title = "Kotlin 2.5.0-Beta1",
        url = "https://github.com/JetBrains/kotlin/releases/tag/v2.5.0-Beta1",
        publishedAt = Instant.parse("2026-09-23T12:02:11Z"),
    )
    private val post = FeedItem(
        key = "https://blog.jetbrains.com/?post_type=kotlin&p=737362",
        title = "Kotlin 2.4.20 Released",
        url = "https://blog.jetbrains.com/kotlin/2026/09/kotlin-2-4-20-released/",
        publishedAt = Instant.parse("2026-09-07T11:30:57Z"),
    )

    private val releasesFeed = feed(FeedSource.KOTLIN_RELEASES, RELEASES_THREAD) { listOf(beta, stable) }
    private val newsFeed = feed(FeedSource.KOTLIN_NEWS, NEWS_THREAD) { listOf(post) }

    @Test
    context(_: MockTransactionContext)
    fun `posts new items oldest first and records message ids`() = runTest {
        val dao = mockk<FeedItemDao>(relaxed = true) {
            coEvery { findKnownKeys(any(), any()) } returns emptySet()
        }

        val job = job(listOf(releasesFeed, newsFeed), dao)
        val _ = job.publish(releasesFeed, since = null)
        val _ = job.publish(newsFeed, since = null)

        assertEquals(
            listOf(
                SentMessage(CHAT_ID, RELEASES_THREAD, "*[Kotlin 2\\.4\\.20](${stable.url})*", "MarkdownV2"),
                SentMessage(CHAT_ID, RELEASES_THREAD, "*[Kotlin 2\\.5\\.0\\-Beta1](${beta.url})*", "MarkdownV2"),
                SentMessage(CHAT_ID, NEWS_THREAD, "*[Kotlin 2\\.4\\.20 Released](${post.url})*", "MarkdownV2"),
            ),
            posted,
        )
        coVerifySequence {
            val _ = dao.findKnownKeys(FeedSource.KOTLIN_RELEASES, listOf(beta.key, stable.key))
            val _ = dao.insert(FeedSource.KOTLIN_RELEASES, stable, CHAT_ID, 1)
            val _ = dao.insert(FeedSource.KOTLIN_RELEASES, beta, CHAT_ID, 2)
            val _ = dao.findKnownKeys(FeedSource.KOTLIN_NEWS, listOf(post.key))
            val _ = dao.insert(FeedSource.KOTLIN_NEWS, post, CHAT_ID, 3)
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `skips known items`() = runTest {
        val dao = mockk<FeedItemDao>(relaxed = true) {
            coEvery { findKnownKeys(FeedSource.KOTLIN_RELEASES, any()) } returns setOf(stable.key)
        }

        val _ = job(listOf(releasesFeed), dao).publish(releasesFeed, since = null)

        assertEquals(listOf("*[Kotlin 2\\.5\\.0\\-Beta1](${beta.url})*"), posted.map { it.text })
        coVerify(exactly = 1) {
            val _ = dao.insert(any(), any(), any(), any())
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `sends plain text when telegram cannot parse markdown and continues`() = runTest {
        val dao = mockk<FeedItemDao>(relaxed = true) {
            coEvery { findKnownKeys(any(), any()) } returns emptySet()
        }
        val kotbot = kotbot { message ->
            if (message.parseMode == "MarkdownV2" && message.text.contains("2\\.4\\.20")) {
                TelegramReply.Error(400, "Bad Request: can't parse entities: Character '.' is reserved")
            } else {
                TelegramReply.Ok
            }
        }

        val _ = job(listOf(releasesFeed), dao, kotbot).publish(releasesFeed, since = null)

        assertEquals(
            listOf(
                SentMessage(CHAT_ID, RELEASES_THREAD, "Kotlin 2.4.20\n${stable.url}", null),
                SentMessage(CHAT_ID, RELEASES_THREAD, "*[Kotlin 2\\.5\\.0\\-Beta1](${beta.url})*", "MarkdownV2"),
            ),
            posted,
        )
        assertEquals(3, requests.size)
        coVerifySequence {
            val _ = dao.findKnownKeys(FeedSource.KOTLIN_RELEASES, any())
            val _ = dao.insert(FeedSource.KOTLIN_RELEASES, stable, CHAT_ID, 1)
            val _ = dao.insert(FeedSource.KOTLIN_RELEASES, beta, CHAT_ID, 2)
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `rejected topic stops the feed without recording and other feeds continue`() = runTest {
        val dao = mockk<FeedItemDao>(relaxed = true) {
            coEvery { findKnownKeys(any(), any()) } returns emptySet()
        }
        val kotbot = kotbot { message ->
            if (message.threadId == RELEASES_THREAD) {
                TelegramReply.Error(400, "Bad Request: message thread not found")
            } else {
                TelegramReply.Ok
            }
        }

        val job = job(listOf(releasesFeed, newsFeed), dao, kotbot)
        val _ = job.publish(releasesFeed, since = null)
        val _ = job.publish(newsFeed, since = null)

        assertEquals(listOf(RELEASES_THREAD, NEWS_THREAD), requests.map { it.threadId })
        assertEquals(listOf(NEWS_THREAD), posted.map { it.threadId })
        coVerify(exactly = 0) {
            val _ = dao.insert(FeedSource.KOTLIN_RELEASES, any(), any(), any())
        }
        coVerify(exactly = 1) {
            val _ = dao.insert(FeedSource.KOTLIN_NEWS, post, CHAT_ID, 1)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    context(_: MockTransactionContext)
    fun `waits retry after and sends again when rate limited`() = runTest {
        val dao = mockk<FeedItemDao>(relaxed = true) {
            coEvery { findKnownKeys(any(), any()) } returns emptySet()
        }
        val kotbot = kotbot {
            if (requests.size == 1) {
                TelegramReply.Error(429, "Too Many Requests: retry after 5", retryAfter = 5)
            } else {
                TelegramReply.Ok
            }
        }

        val _ = job(listOf(newsFeed), dao, kotbot).publish(newsFeed, since = null)

        assertEquals(2, requests.size)
        assertEquals(requests.first(), requests.last())
        assertEquals(1, posted.size)
        assertEquals(5.seconds + 3.seconds, testScheduler.currentTime.milliseconds)
        coVerify(exactly = 1) {
            val _ = dao.insert(FeedSource.KOTLIN_NEWS, post, CHAT_ID, 1)
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `broken or empty feed does not touch database or chat`() = runTest {
        val dao = mockk<FeedItemDao>(relaxed = true)
        val broken = feed(FeedSource.KOTLIN_RELEASES, RELEASES_THREAD) { throw IOException("connection reset") }
        val empty = feed(FeedSource.KOTLIN_NEWS, NEWS_THREAD) { emptyList() }

        val job = job(listOf(broken, empty), dao)
        val _ = job.publish(broken, since = null)
        val _ = job.publish(empty, since = null)

        assertEquals(emptyList<SentMessage>(), requests)
        coVerify(exactly = 0) {
            val _ = dao.findKnownKeys(any(), any())
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `keeps version after posting and skips not modified feed`() = runTest {
        val newest = parseRss(rss).first()
        val dao = daoKnowingAllBlogItemsButNewest()
        val ifNoneMatch = mutableListOf<String?>()
        val blog = blogFeed(ifNoneMatch)
        val job = job(listOf(blog), dao)

        val version = job.publish(blog, since = null)
        val notModifiedVersion = job.publish(blog, since = version)

        assertNotNull(version)
        assertEquals(version, notModifiedVersion)
        assertEquals(listOf(null, "\"blog\""), ifNoneMatch)
        assertEquals(listOf(NEWS_THREAD), posted.map { it.threadId })
        coVerifySequence {
            val _ = dao.findKnownKeys(FeedSource.KOTLIN_NEWS, any())
            val _ = dao.insert(FeedSource.KOTLIN_NEWS, newest, CHAT_ID, 1)
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `drops version without plain text resend when telegram rejects the topic`() = runTest {
        val dao = daoKnowingAllBlogItemsButNewest()
        val blog = blogFeed()
        val kotbot = kotbot { TelegramReply.Error(400, "Bad Request: message thread not found") }

        val version = job(listOf(blog), dao, kotbot).publish(blog, since = null)

        assertEquals(null, version)
        assertEquals(listOf("MarkdownV2"), requests.map { it.parseMode })
        coVerify(exactly = 0) {
            val _ = dao.insert(any(), any(), any(), any())
        }
    }

    @Test
    context(_: MockTransactionContext)
    fun `drops version when network fails`() = runTest {
        val dao = daoKnowingAllBlogItemsButNewest()
        val blog = blogFeed()
        val kotbot = kotbot { TelegramReply.ConnectionReset }

        val version = job(listOf(blog), dao, kotbot).publish(blog, since = null)

        assertEquals(null, version)
        assertEquals(1, requests.size)
        coVerify(exactly = 0) {
            val _ = dao.insert(any(), any(), any(), any())
        }
    }

    @Test
    fun `disabled job never polls`() = runTest {
        var fetches = 0
        val feed = feed(FeedSource.KOTLIN_RELEASES, RELEASES_THREAD) { fetches++; emptyList() }

        job(listOf(feed), mockk(), enabled = false).start()
        delay(1.hours)

        assertEquals(0, fetches)
    }

    @Test
    fun `enabled job polls each feed on start and then on its own interval`() = runTest {
        var releasesFetches = 0
        var newsFetches = 0
        val releases = feed(FeedSource.KOTLIN_RELEASES, RELEASES_THREAD, 5.minutes) { releasesFetches++; emptyList() }
        val news = feed(FeedSource.KOTLIN_NEWS, NEWS_THREAD, 15.minutes) { newsFetches++; emptyList() }

        job(listOf(releases, news), mockk()).start()
        delay(1.minutes)
        assertEquals(1 to 1, releasesFetches to newsFetches)

        delay(15.minutes)
        assertEquals(4 to 2, releasesFetches to newsFetches)
    }

    private companion object {
        private const val CHAT_ID = -1001032833563L
        private const val RELEASES_THREAD = 293400
        private const val NEWS_THREAD = 293499
    }
}
