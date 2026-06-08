package io.heapy.kotbot.infra.configuration

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class BotConfiguration(
    val token: String,
)

@Serializable
data class CasConfiguration(
    /**
     * Single user ID always treated as CAS-flagged — admitted read-only and forced into the appeal
     * flow — without calling the CAS API. For testing the appeal flow; unset (null) in production.
     * Set via the KOTBOT_CAS_FORCE_FLAGGED env.
     */
    val forceFlagged: Long? = null,
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
