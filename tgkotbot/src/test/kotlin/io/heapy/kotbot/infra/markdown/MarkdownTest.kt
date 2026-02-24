package io.heapy.kotbot.infra.markdown

import io.heapy.kotbot.bot.KotbotVerifier
import io.heapy.kotbot.bot.WithKotbotVerifier
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.model.chatId
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@WithKotbotVerifier
class MarkdownTest {
    @Test
    fun KotbotVerifier.`escape simple document`(): Unit = runBlocking {
        val module = createMarkdownModule {}

        val message = """
            Hello, world!

            This message contains special characters: [ ] ( ) ~ ` > # + - = | { } . !

            This is **bold *nested with italic* text** and _italic text with [a link](https://heapy.io)_

            [Visit Heapy's website](https://heapy.io) for more information!

            Here is inline `code` and a code block:

            ```kotlin
            fun main() {
                println("Hello, world!")
            }
            ```

            1. **Bold1**: Statement1.
            2. **Bold2**: Statement2.

            ~strikethrough~

            __underline__

            **bold \*text**

            _italic *text_

            - Item1
            - Item2

            ||SECRET||

            Check support for thematic breaks:

            ---

            Check support for inline HTML:

             <svg stroke-width="2"></svg>

            `inline fixed-width code`

        """.trimIndent()

        val expected = """
            Hello, world\!

            This message contains special characters: \[ \] \( \) \~ \` \> \# \+ \- \= \| \{ \} \. \!

            This is *bold _nested with italic_ text* and _italic text with [a link](https://heapy.io)_

            [Visit Heapy's website](https://heapy.io) for more information\!

            Here is inline `code` and a code block:

            ```kotlin
            fun main() {
                println("Hello, world!")
            }
            ```

            1\. *Bold1*: Statement1\.
            2\. *Bold2*: Statement2\.

            \~strikethrough\~

            *underline*

            *bold \*text*

            _italic \*text_

            \- Item1
            \- Item2

            ||SECRET||

            Check support for thematic breaks:

            \-\-\-

            Check support for inline HTML:

            <svg stroke\-width\="2"\></svg\>

            `inline fixed-width code`

        """.trimIndent()

        val rendered = module.markdown.escape(message)
        assertEquals(expected, rendered)

        kotbot
            .execute(
                SendMessage(
                    chat_id = qaUserId.chatId,
                    text = rendered,
                    parse_mode = ParseMode.MarkdownV2.name,
                    reply_markup = KotbotVerifier.verificationInlineKeyboard,
                )
            )
            .verify()
    }

    @Test
    fun KotbotVerifier.`escape spoiler with rich text`(): Unit = runBlocking {
        val module = createMarkdownModule {}

        val message = """
            ||**bold** inside spoiler||

            ||_italic_ inside spoiler||

            ||[a link](https://heapy.io) inside spoiler||

            ||mixed **bold** and _italic_ and [link](https://heapy.io) content||

            ||special chars: . ! # inside spoiler||

            before ||inline spoiler|| after

            ||multi word spoiler text||

            ||`inline code` inside spoiler||
        """.trimIndent()

        val expected = """
            ||*bold* inside spoiler||

            ||_italic_ inside spoiler||

            ||[a link](https://heapy.io) inside spoiler||

            ||mixed *bold* and _italic_ and [link](https://heapy.io) content||

            ||special chars: \. \! \# inside spoiler||

            before ||inline spoiler|| after

            ||multi word spoiler text||

            ||`inline code` inside spoiler||

        """.trimIndent()

        val rendered = module.markdown.escape(message)
        assertEquals(expected, rendered)

        kotbot
            .execute(
                SendMessage(
                    chat_id = qaUserId.chatId,
                    text = rendered,
                    parse_mode = ParseMode.MarkdownV2.name,
                    reply_markup = KotbotVerifier.verificationInlineKeyboard,
                )
            )
            .verify()
    }
}
