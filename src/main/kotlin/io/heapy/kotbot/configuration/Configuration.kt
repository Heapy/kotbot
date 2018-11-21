package io.heapy.kotbot.configuration

import io.heapy.kotbot.bot.BotConfiguration
import io.heapy.kotbot.metrics.MetricsConfiguration
import java.lang.System.getenv

/**
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
class Configuration(
    override val token: String = getenv("KOTBOT_TOKEN"),
    override val name: String = getenv("KOTBOT_NAME") ?: "KotBot",
    override val environment: String = getenv("KOTBOT_ENV") ?: "development",
    override val version: String = getenv("KOTBOT_VERSION") ?: "0.0.0"
) : BotConfiguration, MetricsConfiguration


