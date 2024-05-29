package io.heapy.kotbot.metrics

import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.micrometer.prometheusmetrics.PrometheusRenameFilter
import kotlinx.serialization.Serializable

@Serializable
data class MetricsConfiguration(
    val tags: Map<String, String>,
)

fun createPrometheusMeterRegistry(
    configuration: MetricsConfiguration,
    prometheusConfig: PrometheusConfig = PrometheusConfig.DEFAULT,
): PrometheusMeterRegistry {
    return PrometheusMeterRegistry(prometheusConfig).also {
        it.config().meterFilter(PrometheusRenameFilter())
        it.config().commonTags(commonTags(configuration))

        // Couple of common metrics
        ClassLoaderMetrics().bindTo(it)
        JvmMemoryMetrics().bindTo(it)
        JvmGcMetrics().bindTo(it)
        ProcessorMetrics().bindTo(it)
        JvmThreadMetrics().bindTo(it)
        UptimeMetrics().bindTo(it)
    }
}

private fun commonTags(
    configuration: MetricsConfiguration,
): List<Tag> {
    return configuration.tags
        .map { (k, v) -> Tag.of(k, v) }
}
