package io.heapy.kotbot.configuration

import io.heapy.komodo.config.dotenv.Dotenv
import io.heapy.kotbot.bot.BotConfiguration
import io.heapy.kotbot.bot.CasConfiguration
import io.heapy.kotbot.bot.FamilyConfiguration
import io.heapy.kotbot.metrics.MetricsConfiguration
import java.nio.file.Paths

private val env = Dotenv(Paths.get("./devops/.env"))

interface Configuration {
    val cas: CasConfiguration
    val metrics: MetricsConfiguration
    val bot: BotConfiguration
    val groups: FamilyConfiguration
}

/**
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
data class DefaultConfiguration(
    override val cas: DefaultCasConfiguration,
    override val metrics: DefaultMetricsConfiguration,
    override val bot: DefaultBotConfiguration,
    override val groups: DefaultFamilyConfiguration
) : Configuration

data class DefaultMetricsConfiguration(
    override val tags: Map<String, String>
) : MetricsConfiguration

data class DefaultCasConfiguration(
    override val allowlist: Set<Long>
) : CasConfiguration

data class DefaultFamilyConfiguration(
    override val ids: Set<Long>,
    override val admins: Map<String, List<Long>>,
) : FamilyConfiguration

data class DefaultBotConfiguration(
    override val token: String = env.get("KOTBOT_TOKEN"),
    override val name: String
) : BotConfiguration

