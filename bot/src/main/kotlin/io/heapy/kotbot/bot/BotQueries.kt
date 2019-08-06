package io.heapy.kotbot.bot

interface BotQueries {
    fun getBotUser(): Pair<Int, String>
    fun isAdminUser(chatId: Long, userId: Int): Boolean
    fun getChatName(chatId: Long): String
}
