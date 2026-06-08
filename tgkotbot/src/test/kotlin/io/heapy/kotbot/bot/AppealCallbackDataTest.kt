package io.heapy.kotbot.bot

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * The admin Approve/Decline buttons round-trip through the same polymorphic [KotbotCallBackData]
 * serializer that `CallbackDataService` uses, so a broken/missing `@Serializable` would surface here.
 */
class AppealCallbackDataTest {
    @Test
    fun `approve appeal callback round-trips through the sealed serializer`() {
        val data: KotbotCallBackData = ApproveAppealCallbackData(sessionId = 4242L)
        val encoded = Json.encodeToString(KotbotCallBackData.serializer(), data)
        val decoded = Json.decodeFromString(KotbotCallBackData.serializer(), encoded)
        assertEquals(data, decoded)
    }

    @Test
    fun `decline appeal callback round-trips through the sealed serializer`() {
        val data: KotbotCallBackData = DeclineAppealCallbackData(sessionId = 7L)
        val encoded = Json.encodeToString(KotbotCallBackData.serializer(), data)
        val decoded = Json.decodeFromString(KotbotCallBackData.serializer(), encoded)
        assertEquals(data, decoded)
    }
}
