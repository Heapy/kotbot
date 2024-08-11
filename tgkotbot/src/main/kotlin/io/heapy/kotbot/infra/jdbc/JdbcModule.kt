package io.heapy.kotbot.infra.jdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.configuration.ConfigurationModule
import io.heapy.kotbot.infra.metrics.MetricsModule
import io.micrometer.core.instrument.binder.db.MetricsDSLContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.PGSimpleDataSource

@Module
open class JdbcModule(
    private val configurationModule: ConfigurationModule,
    private val metricsModule: MetricsModule,
) {
    @OptIn(ExperimentalSerializationApi::class)
    open val configuration: JdbcConfiguration by lazy {
        Hocon.decodeFromConfig(
            JdbcConfiguration.serializer(),
            configurationModule.config.getConfig("jdbc"),
        )
    }

    open val dslContext: DSLContext by lazy {
        System.setProperty("org.jooq.no-logo", "true")
        System.setProperty("org.jooq.no-tips", "true")
        MetricsDSLContext.withMetrics(
            DSL.using(hikariDataSource, SQLDialect.POSTGRES),
            metricsModule.meterRegistry,
            emptyList(),
        )
    }

    open val hikariConfig by lazy {
        HikariConfig().apply {
            dataSourceClassName = PGSimpleDataSource::class.qualifiedName
            username = configuration.user
            password = configuration.password
            dataSourceProperties["databaseName"] = configuration.database
            dataSourceProperties["serverName"] = configuration.host
            dataSourceProperties["portNumber"] = configuration.port
        }
    }

    open val hikariDataSource by lazy {
        HikariDataSource(hikariConfig)
    }
}
