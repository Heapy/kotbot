package io.heapy.kotbot.infra.metrics

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.lifecycle.AutoClosableModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.micrometer.core.instrument.binder.db.PostgreSQLDatabaseMetrics
import io.micrometer.core.instrument.binder.jvm.*
import io.micrometer.core.instrument.binder.logging.LogbackMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics

@Module
open class MetricsReportersModule(
    private val autoClosableModule: AutoClosableModule,
    private val jdbcModule: JdbcModule,
) {
    open val classLoaderMetrics by lazy {
        ClassLoaderMetrics()
    }

    open val jvmMemoryMetrics by lazy {
        JvmMemoryMetrics()
    }

    open val jvmHeapPressureMetrics by lazy {
        autoClosableModule.addClosable(JvmHeapPressureMetrics(), JvmHeapPressureMetrics::close)
    }

    open val jvmInfoMetrics by lazy {
        JvmInfoMetrics()
    }

    open val jvmThreadMetrics by lazy {
        JvmThreadMetrics()
    }

    open val processorMetrics by lazy {
        ProcessorMetrics()
    }

    open val uptimeMetrics by lazy {
        UptimeMetrics()
    }

    open val logbackMetrics by lazy {
        autoClosableModule.addClosable(LogbackMetrics(), LogbackMetrics::close)
    }

    open val postgreSQLDatabaseMetrics by lazy {
        PostgreSQLDatabaseMetrics(
            jdbcModule.hikariDataSource,
            jdbcModule.configuration.database,
        )
    }

    open val meterBinders by lazy {
        listOf(
            classLoaderMetrics,
            jvmMemoryMetrics,
            jvmHeapPressureMetrics,
            jvmInfoMetrics,
            jvmThreadMetrics,
            processorMetrics,
            uptimeMetrics,
            logbackMetrics,
            postgreSQLDatabaseMetrics,
        )
    }

    open val metricsRegister by lazy {
        MetricsRegister(
            meterBinders = meterBinders,
        )
    }
}
