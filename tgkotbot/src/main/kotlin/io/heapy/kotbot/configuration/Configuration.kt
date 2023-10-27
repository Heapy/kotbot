package io.heapy.kotbot.configuration

import io.heapy.kotbot.metrics.MetricsConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val bot: BotConfiguration,
    val cas: CasConfiguration,
    val metrics: MetricsConfiguration,
    val groups: KnownChatsConfiguration,
    val jdbc: JdbcConfiguration,
)

@Serializable
data class BotConfiguration(
    val token: String,
)

@Serializable
data class CasConfiguration(
    val allowlist: Set<Long>,
)

@Serializable
data class KnownChatsConfiguration(
    val admin: Long,
    val forum: Long,
    val ids: Map<String, Long>,
    val blocked: Set<Long>,
    val admins: Map<String, List<Long>>,
)

@Serializable
data class JdbcConfiguration(
    val user: String,
    val password: String,
    val database: String,
    val host: String,
    val port: Int,
)
