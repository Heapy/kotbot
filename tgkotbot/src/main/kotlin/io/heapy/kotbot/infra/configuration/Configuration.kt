package io.heapy.kotbot.infra.configuration

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class BotConfiguration(
    val token: String,
)

@Serializable
data class TestingConfiguration(
    val testUserId: Long? = null,
)

@Serializable
data class KnownChatsConfiguration(
    val notificationChannel: Long,
    val allowedGroups: Map<String, Long>,
)

@Serializable
data class JoinChallengeConfiguration(
    val sessionTimeout: Duration,
    val defaultMaxAttempts: Int,
    val retryCooldown: Duration,
    val chatMaxAttempts: Map<String, Int> = emptyMap(),
    val chatGroups: Map<String, List<String>> = emptyMap(),
)

@Serializable
data class KotlinFeedsConfiguration(
    val enabled: Boolean,
    val chatId: Long,
    val releasesThreadId: Int,
    val newsThreadId: Int,
    val githubToken: String? = null,
    val newsUrls: List<String>,
    val releasesPollInterval: Duration,
    val newsPollInterval: Duration,
    val sendInterval: Duration,
)
