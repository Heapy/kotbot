package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.utils.execAsync
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.telegram.telegrambots.meta.api.methods.GetMe
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * Provides [BotQueries] implementation for Telegram API.
 */
class TelegramApiQueries(private val api: AbsSender, private val http: HttpClient): BotQueries {
    override suspend fun getBotUser(): Pair<Int, String> {
        val bot = api.execAsync(GetMe())
        return bot.id to bot.userName
    }

    override suspend fun isAdminUser(chatId: Long, userId: Int): Boolean {
        val member = api.execAsync(GetChatMember().also {
            it.chatId = chatId.toString()
            it.userId = userId
        })
        val status = member.status
        return status == "creator" || status == "administrator"
    }

    override suspend fun getChatName(chatId: Long): String = api.execAsync(GetChat(chatId)).title

    override suspend fun isCasBanned(userId: Int): Boolean =
        http
            .get<Map<String, Any?>>(CAS_API) {
                parameter(CAS_USER_ID, userId)
            }
            .let { it["ok"] as? Boolean ?: false }

    override fun getCasStatusUrl(userId: Int): String =
        "https://combot.org/cas/query?u=$userId"
}

private const val CAS_API = "https://combot.org/api/cas/check"
private const val CAS_USER_ID = "user_id"
