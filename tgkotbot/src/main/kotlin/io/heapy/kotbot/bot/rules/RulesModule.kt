package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.ChatAdministratorsCache
import io.heapy.kotbot.bot.UserContextServiceModule
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.metrics.MetricsModule

@Module
class RulesModule(
    private val kotbotModule: KotbotModule,
    private val metricsModule: MetricsModule,
    private val daoModule: DaoModule,
    private val userContextServiceModule: UserContextServiceModule,
) {
    val ruleExecutor: RuleExecutor by lazy {
        RuleExecutor(
            rules = rules,
            kotbot = kotbotModule.kotbot,
            meterRegistry = metricsModule.meterRegistry,
            userContextDao = daoModule.userContextDao,
        )
    }

    val rules by lazy {
        listOf(
            bannedUserRule,
            deleteMessageRule,
            deleteGarbageRule,
            deletePropagandaRule,
            tagEnforcementRule,
        )
    }

    val bannedUserRule: Rule by lazy(::BannedUserRule)

    val tagEnforcementRule: Rule by lazy {
        TagEnforcementRule(
            chatAdministratorsCache = chatAdministratorsCache,
        )
    }

    val chatAdministratorsCache: ChatAdministratorsCache by lazy {
        ChatAdministratorsCache(
            kotbot = kotbotModule.kotbot,
        )
    }

    val deleteMessageRule: Rule by lazy(::DeleteMessageRule)

    val deleteGarbageRule: Rule by lazy {
        DeleteGarbageRule(
            garbageMessageDao = daoModule.garbageMessageDao,
            userContextService = userContextServiceModule.userContextService,
        )
    }

    val deletePropagandaRule: Rule by lazy(::DeletePropagandaRule)
}
