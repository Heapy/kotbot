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
import kotlin.time.Duration

class KotlinFeedsJob(
    private val feeds: List<Feed>,
    private val feedItemDao: FeedItemDao,
    private val kotbot: Kotbot,
    private val markdown: Markdown,
    private val transactionProvider: TransactionProvider,
    private val applicationScope: CoroutineScope,
    private val chatId: Long,
    private val pollInterval: Duration,
    private val sendInterval: Duration,
) {
    fun start() {
        applicationScope.launch {
            while (true) {
                publishAll()
                delay(pollInterval)
            }
        }
    }

    internal suspend fun publishAll() {
        for (feed in feeds) {
            try {
                publish(feed)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                log.error("Failed to publish {} feed", feed.source, e)
            }
        }
    }

    private suspend fun publish(feed: Feed) {
        val fetched = feed.fetch()
        if (fetched.isEmpty()) return

        val knownKeys = transactionProvider.transaction {
            feedItemDao.findKnownKeys(feed.source, fetched.map { it.key })
        }

        for (item in selectNewItems(fetched, knownKeys)) {
            // Stop at the first failed send so the remaining items keep their order on the next poll.
            val message = kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(chatId),
                    message_thread_id = feed.threadId,
                    text = markdown.formatFeedItem(item),
                    parse_mode = ParseMode.MarkdownV2.name,
                )
            ) ?: return

            val _ = transactionProvider.transaction {
                feedItemDao.insert(feed.source, item, chatId, message.message_id)
            }
            log.info("Posted {} {} as message {}", feed.source, item.key, message.message_id)

            delay(sendInterval)
        }
    }

    private companion object : Logger()
}

private val COMMONMARK_PUNCTUATION = Regex("""[!-/:-@\[-`{-~]""")

internal fun Markdown.formatFeedItem(item: FeedItem): String {
    val title = item.title.replace(COMMONMARK_PUNCTUATION) { "\\" + it.value }
    return escape("**[$title](${item.url})**").trimEnd()
}
