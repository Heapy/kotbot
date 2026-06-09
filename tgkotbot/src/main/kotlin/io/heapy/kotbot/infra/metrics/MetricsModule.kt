package io.heapy.kotbot.infra.metrics

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.micrometer.prometheusmetrics.PrometheusRenameFilter

@Module
class MetricsModule(
    private val configurationModule: ConfigurationModule,
) {
    val configuration: MetricsConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = MetricsConfiguration.serializer(),
                path = "metrics",
            )
    }

    val prometheusConfig: PrometheusConfig by lazy {
        PrometheusConfig.DEFAULT
    }

    val commonTags: Tags by lazy {
        Tags.of(
            configuration.tags
                .map { [k, v] -> Tag.of(k, v) }
        )
    }

    val meterRegistry by lazy {
        PrometheusMeterRegistry(prometheusConfig).also { meterRegistry ->
            meterRegistry.config().meterFilter(PrometheusRenameFilter())
            meterRegistry.config().commonTags(commonTags)
        }
    }

    val route by lazy {
        MetricsRoute(
            prometheusMeterRegistry = meterRegistry,
        )
    }
}
