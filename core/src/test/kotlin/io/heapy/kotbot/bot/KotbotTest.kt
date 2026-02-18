package io.heapy.kotbot.bot

import io.heapy.komok.tech.config.dotenv.dotenv
import io.heapy.kotbot.bot.method.GetMe
import io.heapy.kotbot.bot.method.GetWebhookInfo
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.method.SendPhoto
import io.heapy.kotbot.bot.model.InputFile
import io.heapy.kotbot.bot.model.LinkPreviewOptions
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.model.chatId
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

/**
 * This test suite is intended to be run manually.
 * Took from [Telek](https://github.com/madhead/telek) project developed by [@madhead](https://github.com/madhead).
 */
@WithKotbotVerifier
class KotbotTest {
    @Test
    fun KotbotVerifier.`execute GetWebhookInfo`() = runBlocking {
        kotbot
            .execute(
                SendMessage(
                    chat_id = qaUserId.chatId,
                    text = """
                Webhook status:

                ```
                ${kotbot.execute(GetWebhookInfo())}
                ```
                """.trimIndent(),
                    parse_mode = "MarkdownV2",
                    reply_markup = KotbotVerifier.verificationInlineKeyboard,
                )
            )
            .verify()
    }

    @Test
    fun KotbotVerifier.`execute GetMe`() = runBlocking {
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
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage using Long chat id`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "This is a simple message send to $qaUserId",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage using String chat id`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = "$qaUserId".chatId,
                text = """This is a simple message send to "$qaUserId"""",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage with MarkdownV2`() = runBlocking {
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
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage with Markdown`() = runBlocking {
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
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage with HTML`() = runBlocking {
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
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage without Preview`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "Link with no preview: https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                link_preview_options = LinkPreviewOptions(
                    is_disabled = true,
                ),
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage with Preview`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "Link with a preview: https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                link_preview_options = LinkPreviewOptions(
                    is_disabled = false,
                ),
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage silent`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "Are you ready for a silent message? Unfocus Telegram client and listen carefully!",
            )
        )

        delay(5.seconds)

        kotbot.execute(
            SendMessage(
                chat_id = qaUserId.chatId,
                text = "This is a silent message",
                disable_notification = true,
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage with Reply`() = runBlocking {
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
                reply_parameters = ReplyParameters(
                    message_id = message.message_id,
                ),
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage to Group`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaGroupId.chatId,
                text = "Message for group $qaGroupId",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage to Channel`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaChannelId.chatId,
                text = "Message for channel $qaChannelId",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendMessage to Public Channel`() = runBlocking {
        kotbot.execute(
            SendMessage(
                chat_id = qaPublicChannelId.chatId,
                text = "Message for public channel $qaPublicChannelId",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendPhoto by URL`() = runBlocking {
        kotbot.execute(
            SendPhoto(
                chat_id = qaUserId.chatId,
                photo = InputFile("https://pixabay.com/get/54e6dc464f54a514f1dc8460da293276103bdee35a5271_640.jpg"),
                caption = "[Photo](https://pixabay.com/photos/milky-way-starry-sky-night-sky-star-2695569) by URL",
                parse_mode = "MarkdownV2",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    fun KotbotVerifier.`execute SendPhoto by Id`() = runBlocking {
        kotbot.execute(
            SendPhoto(
                chat_id = qaUserId.chatId,
                photo = InputFile("AgACAgQAAxkDAAICU15_ZERJnTId9SNEfmKXd8jPyzASAALgqjEb0Un8U9vMJXKk7C4iTpegGwAEAQADAgADeAADUykJAAEYBA"),
                caption = "[Photo](https://pixabay.com/photos/milky-way-starry-sky-night-sky-star-2695569) by file\\_id",
                parse_mode = "MarkdownV2",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    @Test
    @Disabled("Not implemented yet")
    fun KotbotVerifier.`execute SendPhoto by Bytes`() = runBlocking {
        kotbot.execute(
            SendPhoto(
                chat_id = qaUserId.chatId,
                photo = InputFile(""),
                caption = "[Photo](https://pixabay.com/photos/milky-way-starry-sky-night-sky-star-2695569) by local file",
                parse_mode = "MarkdownV2",
                reply_markup = KotbotVerifier.verificationInlineKeyboard,
            )
        ).verify()
    }

    private companion object {
        private val dotenv = dotenv().properties

        private val qaGroupId = dotenv.getValue("KOTBOT_QA_GROUP_ID").toLong()
        private val qaChannelId = dotenv.getValue("KOTBOT_QA_CHANNEL_ID").toLong()
        private val qaPublicChannelId = dotenv.getValue("KOTBOT_QA_PUBLIC_CHANNEL_ID").toLong()
    }
}
