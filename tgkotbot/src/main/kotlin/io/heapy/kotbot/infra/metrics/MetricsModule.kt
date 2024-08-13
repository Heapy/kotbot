package io.heapy.kotbot.infra.metrics

import io.heapy.komok.tech.di.lib.Module
import io.heapy.komok.tech.config.ConfigurationModule
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.micrometer.prometheusmetrics.PrometheusRenameFilter

@Module
open class MetricsModule(
    private val configurationModule: ConfigurationModule,
) {
    open val configuration: MetricsConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = MetricsConfiguration.serializer(),
                path = "metrics",
            )
    }

    open val prometheusConfig: PrometheusConfig by lazy {
        PrometheusConfig.DEFAULT
    }

    open val commonTags: Tags by lazy {
        Tags.of(
            configuration.tags
                .map { (k, v) -> Tag.of(k, v) }
        )
    }

    open val meterRegistry by lazy {
        PrometheusMeterRegistry(prometheusConfig).also { meterRegistry ->
            meterRegistry.config().meterFilter(PrometheusRenameFilter())
            meterRegistry.config().commonTags(commonTags)
        }
    }

    open val route by lazy {
        MetricsRoute(
            prometheusMeterRegistry = meterRegistry,
        )
    }
}
