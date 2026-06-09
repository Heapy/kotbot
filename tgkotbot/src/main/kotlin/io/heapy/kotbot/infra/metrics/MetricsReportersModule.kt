package io.heapy.kotbot.infra.metrics

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.AutoClosableModule
import io.micrometer.core.instrument.binder.db.PostgreSQLDatabaseMetrics
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics
import io.micrometer.core.instrument.binder.jvm.JvmInfoMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.logging.LogbackMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics

@Module
class MetricsReportersModule(
    private val autoClosableModule: AutoClosableModule,
    private val jdbcModule: JdbcModule,
) {
    val classLoaderMetrics by lazy {
        ClassLoaderMetrics()
    }

    val jvmMemoryMetrics by lazy {
        JvmMemoryMetrics()
    }

    val jvmHeapPressureMetrics by lazy {
        autoClosableModule.addClosable(JvmHeapPressureMetrics(), JvmHeapPressureMetrics::close)
    }

    val jvmInfoMetrics by lazy {
        JvmInfoMetrics()
    }

    val jvmThreadMetrics by lazy {
        JvmThreadMetrics()
    }

    val processorMetrics by lazy {
        ProcessorMetrics()
    }

    val uptimeMetrics by lazy {
        UptimeMetrics()
    }

    val logbackMetrics by lazy {
        autoClosableModule.addClosable(LogbackMetrics(), LogbackMetrics::close)
    }

    val postgreSQLDatabaseMetrics by lazy {
        PostgreSQLDatabaseMetrics(
            jdbcModule.hikariDataSource,
            jdbcModule.configuration.database,
        )
    }

    val meterBinders by lazy {
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

    val metricsRegister by lazy {
        MetricsRegister(
            meterBinders = meterBinders,
        )
    }
}
