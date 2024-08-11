package io.heapy.kotbot.infra.metrics

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.configuration.ConfigurationModule
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.micrometer.prometheusmetrics.PrometheusRenameFilter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon

@Module
open class MetricsModule(
    private val configurationModule: ConfigurationModule,
) {
    @OptIn(ExperimentalSerializationApi::class)
    open val configuration: MetricsConfiguration by lazy {
        Hocon.decodeFromConfig(
            MetricsConfiguration.serializer(),
            configurationModule.config.getConfig("metrics"),
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
