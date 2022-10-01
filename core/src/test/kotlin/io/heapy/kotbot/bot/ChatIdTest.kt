package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.StringChatId
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChatIdTest {
    @Serializable
    private data class ChatIdTestObj(
        val chat_id: ChatId,
    )

    @Test
    fun `test deserialization string type`() {
        val obj = Json.decodeFromString(ChatIdTestObj.serializer(), """{"chat_id": "@foo"}""")

        assertEquals(ChatIdTestObj(StringChatId("@foo")), obj)
    }

    @Test
    fun `test deserialization long type`() {
        val obj = Json.decodeFromString(ChatIdTestObj.serializer(), """{"chat_id": 1}""")

        assertEquals(ChatIdTestObj(LongChatId(1)), obj)
    }

    @Test
    fun `test serialization string type`() {
        val json = Json.encodeToString(ChatIdTestObj.serializer(), ChatIdTestObj(StringChatId("@foo")))

        assertEquals("""{"chat_id":"@foo"}""", json)
    }

    @Test
    fun `test serialization long type`() {
        val json = Json.encodeToString(ChatIdTestObj.serializer(), ChatIdTestObj(LongChatId(1)))

        assertEquals("""{"chat_id":1}""", json)
    }
}
