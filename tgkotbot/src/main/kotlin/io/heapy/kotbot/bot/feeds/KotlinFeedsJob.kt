package io.heapy.kotbot.bot.feeds

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TelegramApiError
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.heapy.kotbot.infra.markdown.Markdown
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class KotlinFeedsJob(
    private val enabled: Boolean,
    private val feeds: List<Feed>,
    private val feedItemDao: FeedItemDao,
    private val kotbot: Kotbot,
    private val markdown: Markdown,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
    private val chatId: Long,
    private val sendInterval: Duration,
) {
    private val sendLock = Mutex()

    fun start() {
        if (!enabled) {
            log.info("Kotlin feeds are disabled")
            return
        }

        for (feed in feeds) {
            applicationScope.launch {
                var version: FeedVersion? = null
                while (true) {
                    version = publish(feed, version)
                    delay(feed.pollInterval)
                }
            }
        }
    }

    internal suspend fun publish(
        feed: Feed,
        since: FeedVersion?,
    ): FeedVersion? =
        try {
            sendLock.withLock {
                publishNewItems(feed, since)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log.error("Failed to publish {} feed", feed.source, e)
            null
        }

    private suspend fun publishNewItems(
        feed: Feed,
        since: FeedVersion?,
    ): FeedVersion? {
        val response = when (val fetched = feed.fetch(since)) {
            FeedResponse.NotModified -> return since
            is FeedResponse.Updated -> fetched
        }
        if (response.items.isEmpty()) return response.version

        val knownKeys = transactionProvider.transaction {
            feedItemDao.findKnownKeys(feed.source, response.items.map { it.key })
        }

        for (item in selectNewItems(response.items, knownKeys)) {
            // Stop at the first failed send and drop the version: the next poll refetches everything,
            // so the remaining items are retried in order.
            val message = send(feed, item) ?: return null

            val _ = transactionProvider.transaction {
                feedItemDao.insert(feed.source, item, chatId, message.message_id)
            }
            log.info("Posted {} {} as message {}", feed.source, item.key, message.message_id)

            delay(sendInterval)
        }
        return response.version
    }

    private suspend fun send(
        feed: Feed,
        item: FeedItem,
    ): Message? =
        try {
            try {
                executeRetryingRateLimit(
                    SendMessage(
                        chat_id = LongChatId(chatId),
                        message_thread_id = feed.threadId,
                        text = markdown.formatFeedItem(item),
                        parse_mode = ParseMode.MarkdownV2.name,
                    )
                )
            } catch (e: TelegramApiError) {
                if (!e.isEntityParseError()) throw e
                log.warn("Telegram can't parse {} {}, sending plain text: {}", feed.source, item.key, e.description)
                executeRetryingRateLimit(
                    SendMessage(
                        chat_id = LongChatId(chatId),
                        message_thread_id = feed.threadId,
                        text = "${item.title}\n${item.url}",
                    )
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: TelegramApiError) {
            log.warn(
                "Telegram rejected {} {}: {} {}",
                feed.source,
                item.key,
                e.errorCode ?: e.httpStatusCode,
                e.description,
            )
            null
        } catch (e: Exception) {
            // Ktor timeout messages contain the request URL, and the Telegram URL contains the bot token.
            log.warn("Failed to send {} {}: {}", feed.source, item.key, e::class.simpleName)
            null
        }

    private suspend fun executeRetryingRateLimit(message: SendMessage): Message =
        try {
            kotbot.execute(message)
        } catch (e: TelegramApiError) {
            val retryAfter = e.retryAfter
            if (e.errorCode != TOO_MANY_REQUESTS || retryAfter == null) throw e
            delay(retryAfter.seconds)
            kotbot.execute(message)
        }

    private fun TelegramApiError.isEntityParseError(): Boolean =
        errorCode == BAD_REQUEST && description?.startsWith("Bad Request: can't parse entities") == true

    private companion object : Logger() {
        private const val BAD_REQUEST = 400
        private const val TOO_MANY_REQUESTS = 429
    }
}

private val COMMONMARK_PUNCTUATION = Regex("""[!-/:-@\[-`{-~]""")

internal fun Markdown.formatFeedItem(item: FeedItem): String {
    val title = item.title.replace(COMMONMARK_PUNCTUATION) { "\\" + it.value }
    return escape("**[$title](${item.url})**").trimEnd()
}
