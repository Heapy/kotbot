package io.heapy.tgpt.infra.metrics

import io.micrometer.core.instrument.binder.MeterBinder
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

class MetricsRegister(
    private val meterBinders: List<MeterBinder>,
) {
    fun addMetricsToRegistry(
        prometheusMeterRegistry: PrometheusMeterRegistry,
    ) {
        meterBinders.forEach { binder ->
            binder.bindTo(prometheusMeterRegistry)
        }
    }
}
