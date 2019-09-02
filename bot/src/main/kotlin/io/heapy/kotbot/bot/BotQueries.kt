package io.heapy.kotbot.bot

/**
 * Provides a way to execute idempotent queries against messenger authority.
 */
interface BotQueries {
    suspend fun getBotUser(): Pair<Int, String>
    suspend fun isAdminUser(chatId: Long, userId: Int): Boolean
    suspend fun getChatName(chatId: Long): String

    suspend fun isCasBanned(userId: Int): Boolean
    fun getCasStatusUrl(userId: Int): String
}
