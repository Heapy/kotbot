package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.RichBlock
import io.heapy.kotbot.bot.model.RichBlockParagraph
import io.heapy.kotbot.bot.model.RichText
import io.heapy.kotbot.bot.model.RichTextBold
import io.heapy.kotbot.bot.model.RichTextList
import io.heapy.kotbot.bot.model.RichTextString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RichBlockSerializerTest {
    @Test
    fun `test deserialization paragraph block`() {
        val block = Json.decodeFromString(
            RichBlock.serializer(),
            """{"type":"paragraph","text":"Hello"}""",
        )

        assertEquals(
            RichBlockParagraph(
                type = "paragraph",
                text = RichTextString("Hello"),
            ),
            block,
        )
    }

    @Test
    fun `test deserialization rich text list`() {
        val text = Json.decodeFromString(
            RichText.serializer(),
            """["Hello",{"type":"bold","text":"world"}]""",
        )

        assertEquals(
            RichTextList(
                listOf(
                    RichTextString("Hello"),
                    RichTextBold(
                        type = "bold",
                        text = RichTextString("world"),
                    ),
                )
            ),
            text,
        )
    }

    @Test
    fun `test serialization paragraph block`() {
        val json = Json.encodeToString(
            RichBlock.serializer(),
            RichBlockParagraph(
                type = "paragraph",
                text = RichTextString("Hello"),
            ),
        )

        assertEquals("""{"type":"paragraph","text":"Hello"}""", json)
    }
}
