package io.heapy.kotbot.metrics

import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micrometer.prometheus.PrometheusRenameFilter

/**
 * Provides configured instance of prometheus registry.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
fun createPrometheusMeterRegistry(
    configuration: MetricsConfiguration,
    prometheusConfig: PrometheusConfig = PrometheusConfig.DEFAULT
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
    }
}

private fun commonTags(
    configuration: MetricsConfiguration
): List<Tag> {
    return configuration.tags
        .map { (k, v) -> Tag.of(k, v) }
}
