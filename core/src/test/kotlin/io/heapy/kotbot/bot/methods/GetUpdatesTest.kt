package io.heapy.kotbot.bot.methods

import io.heapy.kotbot.bot.kotbotJson
import io.heapy.kotbot.bot.method.GetUpdates
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetUpdatesTest {
    @Test
    fun `test get updates method`() {
        val request = kotbotJson.encodeToString(
            GetUpdates.serializer(),
            GetUpdates(
                offset = 1,
                limit = 100,
                timeout = 0,
                allowed_updates = listOf("message", "edited_channel_post", "callback_query"),
            )
        )

        assertEquals(
            """{"offset":1,"limit":100,"timeout":0,"allowed_updates":["message","edited_channel_post","callback_query"]}""",
            request
        )
    }
}
