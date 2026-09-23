package io.heapy.kotbot.bot.feeds

import io.heapy.kotbot.infra.configuration.KotlinFeedsConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class KotlinFeedsConfigurationTest {
    @Test
    fun `reads kotlin feeds configuration from reference conf`() {
        val configuration = createKotlinFeedsModule {}.kotlinFeedsConfiguration

        assertEquals(
            KotlinFeedsConfiguration(
                chatId = -1001032833563,
                releasesThreadId = 293400,
                newsThreadId = 293499,
                releasesUrl = "https://api.github.com/repos/JetBrains/kotlin/releases?per_page=30",
                newsUrls = listOf(
                    "https://blog.jetbrains.com/kotlin/feed/",
                    "https://feeds.feedburner.com/kotlin",
                ),
                pollInterval = 15.minutes,
                sendInterval = 3.seconds,
            ),
            configuration,
        )
    }
}
