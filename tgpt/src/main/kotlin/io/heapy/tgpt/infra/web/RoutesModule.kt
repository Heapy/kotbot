package io.heapy.tgpt.infra.web

import io.heapy.komok.tech.di.lib.Module
import io.heapy.tgpt.bot.admin.AdminModule
import io.heapy.tgpt.infra.health_check.HealthCheckModule
import io.heapy.tgpt.infra.metrics.MetricsModule

@Module
open class RoutesModule(
    private val metricsModule: MetricsModule,
    private val healthCheckModule: HealthCheckModule,
    private val adminModule: AdminModule,
) {
    open val routes by lazy {
        listOf(
            metricsModule.route,
            healthCheckModule.route,
            adminModule.route,
        )
    }
}
