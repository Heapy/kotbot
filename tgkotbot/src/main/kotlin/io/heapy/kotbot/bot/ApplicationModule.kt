package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.lifecycle.AutoClosableModule
import io.heapy.kotbot.infra.lifecycle.UptimeModule
import io.heapy.kotbot.infra.metrics.MetricsModule
import io.heapy.kotbot.infra.metrics.MetricsReportersModule
import io.heapy.kotbot.infra.web.ServerModule
import runMigrations
import kotlin.concurrent.thread

@Module
open class ApplicationModule(
    private val serverModule: ServerModule,
    private val jdbcModule: JdbcModule,
    private val metricsModule: MetricsModule,
    private val metricsReportersModule: MetricsReportersModule,
    private val autoClosableModule: AutoClosableModule,
    private val uptimeModule: UptimeModule,
    private val kotlinChatBotModule: KotlinChatBotModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val updateProcessorsModule: UpdateProcessorsModule,
) {
    open suspend fun start() {
        metricsReportersModule
            .metricsRegister
            .addMetricsToRegistry(
                metricsModule.meterRegistry
            )

        runMigrations(jdbcModule.hikariDataSource)

        Runtime.getRuntime().addShutdownHook(thread(
            start = false,
            name = "kotbot-shutdown-hook",
        ) {
            log.info("Shutdown hook called.")
            autoClosableModule.close()
        })

        serverModule.server.start()
        updateProcessorsModule.updateProcessor.start()
        kotlinChatBotModule.kotlinChatsBot.start()

        log.info("Application started in ${uptimeModule.uptimeService.uptime}ms.")

        applicationScopeModule.applicationJob.join()

        log.info("Application stopped.")
    }

    private companion object : Logger()
}
