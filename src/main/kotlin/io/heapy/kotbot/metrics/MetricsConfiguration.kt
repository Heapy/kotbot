package io.heapy.kotbot.metrics

/**
 * Configuration required for metrics.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
interface MetricsConfiguration {
    val tags: Map<String, String>
}
