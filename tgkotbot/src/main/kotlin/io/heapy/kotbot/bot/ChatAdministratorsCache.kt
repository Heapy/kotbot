package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.GetChatAdministrators
import io.heapy.kotbot.bot.model.ChatMember
import io.heapy.kotbot.bot.model.ChatMemberAdministrator
import io.heapy.kotbot.bot.model.ChatMemberOwner
import io.heapy.kotbot.bot.model.LongChatId
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/**
 * Default time a chat's administrator list is cached before it is refreshed from Telegram.
 *
 * Administrators change rarely, so this trades a small staleness window for far fewer
 * [GetChatAdministrators] calls — the alternative is one API request per processed message.
 */
val DEFAULT_ADMIN_CACHE_TTL: Duration = 1.days

/**
 * Per-chat cache of administrator (and owner) user ids, backed by [GetChatAdministrators].
 *
 * Lets callers skip Telegram-API actions that only the chat creator may perform on other
 * administrators — e.g. `setChatMemberTag`, which fails with `CHAT_CREATOR_REQUIRED` when the
 * target is an admin or the owner. Entries expire after [ttl]; a failed lookup is not cached so
 * it is retried on the next call. [nanoTime] is injectable for tests.
 */
class ChatAdministratorsCache(
    private val kotbot: Kotbot,
    private val ttl: Duration = DEFAULT_ADMIN_CACHE_TTL,
    private val nanoTime: () -> Long = System::nanoTime,
) {
    private class Entry(
        val adminIds: Set<Long>,
        val expiresAtNanos: Long,
    )

    private val byChat = ConcurrentHashMap<Long, Entry>()

    /**
     * Whether [userId] is an administrator or the owner of [chatId].
     *
     * Returns `false` when the administrator list cannot be fetched, so callers degrade to their
     * previous behaviour rather than skipping everyone on a transient API error.
     */
    suspend fun isAdministrator(
        chatId: Long,
        userId: Long,
    ): Boolean =
        adminIds(chatId)?.contains(userId) ?: false

    private suspend fun adminIds(
        chatId: Long,
    ): Set<Long>? {
        val now = nanoTime()
        byChat[chatId]?.let { entry ->
            // Subtraction (not direct comparison) keeps this correct across nanoTime() sign flips.
            if (entry.expiresAtNanos - now > 0) {
                return entry.adminIds
            }
        }

        val members = kotbot.executeSafely(
            GetChatAdministrators(chat_id = LongChatId(chatId)),
        ) ?: return null

        val ids = members.mapNotNull(ChatMember::adminUserId).toSet()
        byChat[chatId] = Entry(
            adminIds = ids,
            expiresAtNanos = now + ttl.inWholeNanoseconds,
        )
        return ids
    }
}

private fun ChatMember.adminUserId(): Long? =
    when (this) {
        is ChatMemberOwner -> user.id
        is ChatMemberAdministrator -> user.id
        else -> null
    }
