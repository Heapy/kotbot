package io.heapy.kotbot.bot.feeds

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
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
            val message = kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(chatId),
                    message_thread_id = feed.threadId,
                    text = markdown.formatFeedItem(item),
                    parse_mode = ParseMode.MarkdownV2.name,
                )
            ) ?: return null

            val _ = transactionProvider.transaction {
                feedItemDao.insert(feed.source, item, chatId, message.message_id)
            }
            log.info("Posted {} {} as message {}", feed.source, item.key, message.message_id)

            delay(sendInterval)
        }
        return response.version
    }

    private companion object : Logger()
}

private val COMMONMARK_PUNCTUATION = Regex("""[!-/:-@\[-`{-~]""")

internal fun Markdown.formatFeedItem(item: FeedItem): String {
    val title = item.title.replace(COMMONMARK_PUNCTUATION) { "\\" + it.value }
    return escape("**[$title](${item.url})**").trimEnd()
}
