package io.heapy.kotbot.bot

/**
 * Provides a way to execute idempotent queries against messenger authority.
 */
interface BotQueries {
    fun getBotUser(): Pair<Int, String>
    fun isAdminUser(chatId: Long, userId: Int): Boolean
    fun getChatName(chatId: Long): String
}
