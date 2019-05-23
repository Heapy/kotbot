package io.heapy.kotbot.configuration

import io.heapy.komodo.config.dotenv.Dotenv
import io.heapy.kotbot.bot.BotConfiguration
import io.heapy.kotbot.metrics.MetricsConfiguration
import java.nio.file.Paths

private val env = Dotenv(Paths.get("./devops/.env"))

/**
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
class Configuration(
    override val token: String = env.get("KOTBOT_TOKEN"),
    override val name: String = env.getOrNull("KOTBOT_NAME") ?: "KotBot",
    override val version: String = env.get("KOTBOT_RELEASE")
) : BotConfiguration, MetricsConfiguration


