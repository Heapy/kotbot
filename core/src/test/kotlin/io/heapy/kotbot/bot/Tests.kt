package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.*
import io.heapy.kotbot.bot.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

val verificationInlineKeyboard = InlineKeyboardMarkup(
    inline_keyboard = listOf(
        listOf(
            InlineKeyboardButton(text = "PASS", callback_data = "PASS"),
            InlineKeyboardButton(text = "FAIL", callback_data = "FAIL")
        ),
    ),
)

/**
 * This test suite is intended to be run manually.
 * Took from [Telek](https://github.com/madhead/telek) project developed by [@madhead](https://github.com/madhead).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class KotbotTest {
    @Test
    @Order(1)
    fun `execute GetWebhookInfo`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = """
                Webhook status:

                ```
                ${kotbot.execute(GetWebhookInfo())}
                ```
            """.trimIndent(),
                parse_mode = "MarkdownV2",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(2)
    fun `execute GetMe`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = """
                Bot info:
                
                ```
                ${kotbot.execute(GetMe())}
                ```
            """.trimIndent(),
                parse_mode = "MarkdownV2",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(3)
    fun `execute SendMessage using Long chat id`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "This is a simple message send to $qaUserId",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(4)
    fun `execute SendMessage using String chat id`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = "$qaUserId".chatId,
                text = """This is a simple message send to "$qaUserId"""",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(5)
    fun `execute SendMessage with MarkdownV2`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = """
                *MarkdownV2 test*

                Italic: _italic_
                Underline: __underline__
                Strikethrough: ~strikethrough~
                Markup nesting: *bold _italic bold ~italic bold strikethrough~ __underline italic bold___ bold*

                [Google](https://google.com)

                Your TG profile: [$qaUserId](tg://user?id=$qaUserId)

                `rm -rf /`

                ```
                data class User(
                    val id: Int,
                    val name: String,
                )
                ```

                ```kotlin
                data class User(
                    val id: Int,
                    val name: String,
                )
                ```
            """.trimIndent(),
                parse_mode = "MarkdownV2",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(6)
    fun `execute SendMessage with Markdown`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = """
                *Markdown test*

                Italic: _italic_

                [Google](https://google.com)

                Your TG profile: [$qaUserId](tg://user?id=$qaUserId)

                `rm -rf /`

                ```
                data class User(
                    val id: Int,
                    val name: String
                )
                ```

                ```kotlin
                data class User(
                    val id: Int,
                    val name: String
                )
                ```
            """.trimIndent(),
                parse_mode = "Markdown",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(7)
    fun `execute SendMessage with HTML`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = """
                <b>HTML test</b>

                Strong: <strong>strong</strong>
                Italic: <i>italic</i>
                Emphasis: <em>emphasis</em>
                Underline: <u>underline</u>
                Inserted: <ins>inserted</ins>
                Strikethrough (&lt;s&gt;): <s>strikethrough</s>
                Strikethrough (&lt;strike&gt;): <strike>strikethrough</strike>
                Deleted: <del>strikethrough</del>
                <b>bold <i>italic bold <s>italic bold strikethrough</s> <u>underline italic bold</u></i> bold</b>

                <a href="https://google.com">Google</a>

                Your TG profile: <a href="tg://user?id=$qaUserId">$qaUserId</a>

                <code>rm -rf /</code>

                <pre>
                data class User(
                    val id: Int,
                    val name: String
                )
                </pre>

                <pre>
                    <code class="language-kotlin">
                data class User(
                    val id: Int,
                    val name: String
                )
                    </code>
                </pre>
                """.trimIndent(),
                parse_mode = "HTML",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(8)
    fun `execute SendMessage without Preview`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "Link with no preview: https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                disable_web_page_preview = true,
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(9)
    fun `execute SendMessage with Preview`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "Link with a preview: https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                disable_web_page_preview = false,
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(10)
    fun `execute SendMessage silent`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "Are you ready for a silent message? Unfocus Telegram client and listen carefully!",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()

        delay(5000)

        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "This is a silent message",
                disable_notification = true,
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(11)
    fun `execute SendMessage with Reply`() = runBlocking {
        val message = kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "The bot will reply to this message in 1 second",
            )
        )

        delay(1000)

        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "This is a reply",
                reply_to_message_id = message.message_id,
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(12)
    fun `execute SendMessage to Group`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaGroupId.chatId,
                text = "Message for group $qaGroupId",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(13)
    fun `execute SendMessage to Channel`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaChannelId.chatId,
                text = "Message for channel $qaChannelId",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(14)
    fun `execute SendMessage to Public Channel`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaPublicChannelId.chatId,
                text = "Message for public channel $qaPublicChannelId",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(15)
    fun `execute SendPhoto by URL`() = runBlocking {
        kotbot.execute(
            SendPhoto(
                chat_id = qaUserId.chatId,
                photo = InputFile("https://pixabay.com/get/54e6dc464f54a514f1dc8460da293276103bdee35a5271_640.jpg"),
                caption = "[Photo](https://pixabay.com/photos/milky-way-starry-sky-night-sky-star-2695569) by URL",
                parse_mode = "MarkdownV2",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(16)
    fun `execute SendPhoto by Id`() = runBlocking {
        kotbot.execute(
            SendPhoto(
                chat_id = qaUserId.chatId,
                photo = InputFile("AgACAgQAAxkDAAICU15_ZERJnTId9SNEfmKXd8jPyzASAALgqjEb0Un8U9vMJXKk7C4iTpegGwAEAQADAgADeAADUykJAAEYBA"),
                caption = "[Photo](https://pixabay.com/photos/milky-way-starry-sky-night-sky-star-2695569) by file\\_id",
                parse_mode = "MarkdownV2",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Order(17)
    @Disabled("Not implemented yet")
    fun `execute SendPhoto by Bytes`() = runBlocking {
        kotbot.execute(
            SendPhoto(
                chat_id = qaUserId.chatId,
                photo = InputFile(""),
                caption = "[Photo](https://pixabay.com/photos/milky-way-starry-sky-night-sky-star-2695569) by local file",
                parse_mode = "MarkdownV2",
                reply_markup = verificationInlineKeyboard,
            )
        ).verify()
    }

    companion object {
        private var offset: Int? = null
        private val log = logger<KotbotTest>()

        private val env = dotenv()

        private val kotbot = Kotbot(
            token = env.getValue("KOTBOT_TOKEN")
        )

        private val qaUserId = env.getValue("KOTBOT_QA_USER_ID").toLong()
        private val qaGroupId = env.getValue("KOTBOT_QA_GROUP_ID").toLong()
        private val qaChannelId = env.getValue("KOTBOT_QA_CHANNEL_ID").toLong()
        private val qaPublicChannelId = env.getValue("KOTBOT_QA_PUBLIC_CHANNEL_ID").toLong()

        @JvmStatic
        @BeforeAll
        fun setUp() = runBlocking {
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

            kotbot.execute(
                SendMessage(
                    chat_id = qaUserId.chatId,
                    text = """
                        *Kotbot smoke test started\!*
                        
                        Github run [${env["GITHUB_RUN_ID"]}](${env["GITHUB_SERVER_URL"]}/${env["GITHUB_REPOSITORY"]}/actions/runs/${env["GITHUB_RUN_ID"]})
                    """.trimIndent(),
                    parse_mode = "MarkdownV2",
                    disable_web_page_preview = true,
                )
            )
            Unit
        }

        @JvmStatic
        @AfterAll
        fun tearDown() = runBlocking {
            try {
                kotbot.execute(
                    SendMessage(
                        chat_id = qaUserId.chatId,
                        text = """
                            *Kotbot smoke test finished\!*
                            
                            Github run [${env["GITHUB_RUN_ID"]}](${env["GITHUB_SERVER_URL"]}/${env["GITHUB_REPOSITORY"]}/actions/runs/${env["GITHUB_RUN_ID"]})
                        """.trimIndent(),
                        parse_mode = "MarkdownV2",
                        disable_web_page_preview = true
                    )
                )
            } finally {
                kotbot.httpClient.use {}
            }
            Unit
        }
    }

    private suspend fun Message.verify() {
        val message = this
        while (true) {
            kotbot
                .execute(
                    GetUpdates(
                        offset = offset,
                        allowed_updates = listOf("callback_query")
                    )
                )
                .onEach {
                    offset = it.update_id + 1
                    println("Received update: ${it.update_id}")
                }
                .find {
                    (it.callback_query?.message?.message_id == message.message_id) &&
                            (it.callback_query?.from?.id == qaUserId)
                }
                ?.let { update ->
                    assertTrue(
                        kotbot.execute(
                            AnswerCallbackQuery(
                                callback_query_id = update.callback_query?.id!!
                            )
                        )
                    )
                    assertEquals("PASS", update.callback_query?.data)

                    return
                }
        }
    }
}

