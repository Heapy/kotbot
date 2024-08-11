package io.heapy.kotbot.infra.metrics

import kotlinx.serialization.Serializable

@Serializable
data class MetricsConfiguration(
    val tags: Map<String, String>,
)
