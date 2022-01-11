package io.heapy.kotbot.configuration

import io.heapy.kotbot.metrics.MetricsConfiguration

data class Configuration(
    val bot: BotConfiguration,
    val cas: CasConfiguration,
    val metrics: MetricsConfiguration,
    val groups: KnownChatsConfiguration,
)

data class BotConfiguration(
    val token: String,
)

data class CasConfiguration(
    val allowlist: Set<Long>,
)

data class KnownChatsConfiguration(
    val admin: Long,
    val ids: Map<String, Long>,
    val blocked: Set<Long>,
    val admins: Map<String, List<Long>>,
)

