package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.database.enums.FeedSource
import io.heapy.kotbot.database.tables.FeedItem.Companion.FEED_ITEM
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import java.time.LocalDateTime

class FeedItemDao {
    context(_: TransactionContext)
    suspend fun findKnownKeys(
        source: FeedSource,
        keys: Collection<String>,
    ): Set<String> = useTx {
        dslContext
            .select(FEED_ITEM.ITEM_KEY)
            .from(FEED_ITEM)
            .where(FEED_ITEM.SOURCE.eq(source))
            .and(FEED_ITEM.ITEM_KEY.`in`(keys))
            .fetchSet(FEED_ITEM.ITEM_KEY)
            .filterNotNull()
            .toSet()
    }

    context(_: TransactionContext)
    suspend fun insert(
        source: FeedSource,
        item: FeedItem,
        chatId: Long,
        messageId: Int,
    ): Int = useTx {
        dslContext
            .insertInto(
                FEED_ITEM,
                FEED_ITEM.SOURCE,
                FEED_ITEM.ITEM_KEY,
                FEED_ITEM.TITLE,
                FEED_ITEM.URL,
                FEED_ITEM.CHAT_ID,
                FEED_ITEM.MESSAGE_ID,
                FEED_ITEM.POSTED_AT,
            )
            .values(
                source,
                item.key,
                item.title,
                item.url,
                chatId,
                messageId,
                LocalDateTime.now(),
            )
            .onConflict(FEED_ITEM.SOURCE, FEED_ITEM.ITEM_KEY)
            .doNothing()
            .execute()
    }
}
