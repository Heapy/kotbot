package io.heapy.kotbot.infra.web

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.admin.AdminModule
import io.heapy.kotbot.bot.events.EventsModule
import io.heapy.kotbot.infra.health_check.HealthCheckModule
import io.heapy.kotbot.infra.metrics.MetricsModule

@Module
open class RoutesModule(
    private val metricsModule: MetricsModule,
    private val healthCheckModule: HealthCheckModule,
    private val eventsModule: EventsModule,
    private val adminModule: AdminModule,
) {
    open val routes by lazy {
        listOf(
            metricsModule.route,
            healthCheckModule.route,
            eventsModule.route,
            adminModule.route,
        )
    }
}
