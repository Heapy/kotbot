package io.heapy.tgpt.infra.metrics

import kotlinx.serialization.Serializable

@Serializable
data class MetricsConfiguration(
    val tags: Map<String, String>,
)
