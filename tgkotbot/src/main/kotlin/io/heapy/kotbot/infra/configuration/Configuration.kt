package io.heapy.kotbot.infra.configuration

import kotlinx.serialization.Serializable

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
    val notificationChannel: Long,
    val allowedGroups: Map<String, Long>,
    val admins: Map<String, List<Long>>,
)
