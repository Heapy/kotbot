package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.AnswerCallbackQuery
import io.heapy.kotbot.bot.method.GetUpdates
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.InaccessibleMessage
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LinkPreviewOptions
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.model.chatId
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class KotbotVerifier(
    val env: Map<String, String>,
    val kotbot: Kotbot,
    val qaUserId: Long,
) : AutoCloseable {
    init {
        drain()
        sendNotification("*Kotbot smoke test started\\!*")
    }

    private fun drain() = runBlocking {
        log.info("Drain updates")

        kotbot
            .execute(
                GetUpdates(
                    limit = 100,
                    timeout = 0,
                )
            )
            .also {
                log.info("Drained updates: {}", it.size)
            }
    }

    private fun sendNotification(
        message: String,
    ) = runBlocking {
        val message = buildString {
            appendLine(message)
            appendLine()
            if (env["GITHUB_RUN_ID"] != null) {
                appendLine("Github run [${env["GITHUB_RUN_ID"]}](${env["GITHUB_SERVER_URL"]}/${env["GITHUB_REPOSITORY"]}/actions/runs/${env["GITHUB_RUN_ID"]})")
            }
        }

        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = message,
                parse_mode = ParseMode.MarkdownV2.name,
                link_preview_options = LinkPreviewOptions(
                    is_disabled = true,
                ),
            )
        )
    }

    @OptIn(ExperimentalAtomicApi::class)
    private val offset = AtomicInt(0)

    @OptIn(ExperimentalAtomicApi::class)
    suspend fun Message.verify(
        expectedData: String = "PASS",
    ) {
        val message = this
        while (true) {
            kotbot
                .execute(
                    GetUpdates(
                        offset = offset.load().takeIf { it > 0 },
                        allowed_updates = listOf("callback_query")
                    )
                )
                .onEach { update ->
                    offset.store(update.update_id + 1)
                    log.info("Received update: ${update.update_id}")
                }
                .find {
                    when (val callbackMessage = it.callback_query?.message) {
                        is InaccessibleMessage if callbackMessage.message_id == message.message_id
                            && it.callback_query.from.id == qaUserId -> true

                        is Message if callbackMessage.message_id == message.message_id
                            && it.callback_query.from.id == qaUserId -> true

                        else -> false
                    }
                }
                ?.let { update ->
                    assertTrue(
                        kotbot.execute(
                            AnswerCallbackQuery(
                                callback_query_id = update.callback_query?.id
                                    ?: error("Expected only callback query messages"),
                            )
                        )
                    )

                    assertEquals(expectedData, update.callback_query.data)

                    return
                }
        }
    }

    override fun close() {
        try {
            sendNotification("*Kotbot smoke test finished\\!*")
        } finally {
            kotbot.httpClient.use {}
        }
    }

    companion object {
        private val log = logger<KotbotVerifier>()

        val verificationInlineKeyboard = InlineKeyboardMarkup(
            inline_keyboard = listOf(
                listOf(
                    InlineKeyboardButton(text = "PASS", callback_data = "PASS"),
                    InlineKeyboardButton(text = "FAIL", callback_data = "FAIL")
                ),
            ),
        )
    }
}
