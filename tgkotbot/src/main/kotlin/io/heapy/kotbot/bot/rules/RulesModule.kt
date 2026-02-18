package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.UserContextServiceModule
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.HttpClientModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.configuration.CasConfiguration
import io.heapy.kotbot.infra.metrics.MetricsModule

@Module
open class RulesModule(
    private val configurationModule: ConfigurationModule,
    private val httpClientModule: HttpClientModule,
    private val kotbotModule: KotbotModule,
    private val metricsModule: MetricsModule,
    private val daoModule: DaoModule,
    private val userContextServiceModule: UserContextServiceModule,
) {
    open val ruleExecutor: RuleExecutor by lazy {
        RuleExecutor(
            rules = rules,
            kotbot = kotbotModule.kotbot,
            meterRegistry = metricsModule.meterRegistry,
        )
    }

    open val rules by lazy {
        listOf(
            deleteMessageRule,
            deleteGarbageRule,
            combotCasRule,
            deletePropagandaRule,
        )
    }

    open val deleteMessageRule: Rule by lazy(::DeleteMessageRule)

    open val deleteGarbageRule: Rule by lazy {
        DeleteGarbageRule(
            garbageMessageDao = daoModule.garbageMessageDao,
            userContextService = userContextServiceModule.userContextService,
        )
    }

    open val deletePropagandaRule: Rule by lazy(::DeletePropagandaRule)

    open val combotCasRule: Rule by lazy {
        CombotCasRule(
            client = httpClientModule.httpClient,
            casConfiguration = casConfiguration,
        )
    }

    open val casConfiguration: CasConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = CasConfiguration.serializer(),
                path = "cas",
            )
    }
}
