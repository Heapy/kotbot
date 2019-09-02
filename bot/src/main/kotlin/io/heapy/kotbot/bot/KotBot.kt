package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.rule.*
import io.heapy.kotbot.bot.utils.execAsync
import io.heapy.logging.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import kotlinx.coroutines.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ForwardMessage
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

/**
 * @author Ruslan Ibragimov
 */
class KotBot(
    private val configuration: BotConfiguration,
    private val rules: List<Rule>,
    private val state: State
) : TelegramLongPollingBot() {
    override fun getBotToken() = configuration.token
    override fun getBotUsername() = configuration.name

    private val httpClient = HttpClient(CIO) {
        /*install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }*/
        install(JsonFeature) {
            serializer = JacksonSerializer()
            acceptContentTypes = listOf(ContentType.Any)
        }
    }

    private val queries = TelegramApiQueries(this, httpClient)

    init {
        runBlocking {
            val (botUserId, botUserName) = queries.getBotUser()
            state.botUserId = botUserId
            state.botUserName = botUserName
            LOGGER.info { "Bot info: @${botUserName} [${botUserId}]" }
        }
    }

    override fun onUpdateReceived(update: Update) = runBlocking {
        LOGGER.debug { update.toString() }

        rules
            .flatMap { rule -> rule.validate(update, queries) }
            .distinct()
            .map { async { executeAction(it) } }
            .awaitAll()

        Unit
    }

    private suspend fun executeAction(action: Action): Unit = try {
        when (action) {
            is DeleteMessageAction -> {
                execAsync(DeleteMessage(action.chatId, action.messageId))
            }
            is KickUserAction -> {
                execAsync(KickChatMember(action.chatId, action.userId))
            }
            is SendMessageAction -> {
                execAsync(SendMessage(action.chatId, action.text).also {
                    if(action.inlineKeyboard != null) {
                        it.replyMarkup = InlineKeyboardMarkup().apply { keyboard = action.inlineKeyboard }
                    }
                })
            }
            is ForwardMessageAction -> {
                execAsync(ForwardMessage(action.chatId, action.fromChatId, action.messageId))
            }
        }
        Unit
    } catch (e: TelegramApiRequestException) {
        val code = TelegramError.byCode(e.errorCode)
        when {
            code == TelegramError.BadRequest && action is ChatAction -> {
                // assuming the bot is not an admin, repeat the action later
                state.deferAction(action.chatId, action)
            }
            else -> LOGGER.error(e) { "Unable to execute action $action, errorCode: ${e.errorCode}" }
        }
    } catch (e: Exception) {
        LOGGER.error(e) { "Unable to execute action $action" }
    }

    companion object {
        private val LOGGER = logger<KotBot>()
    }
}
