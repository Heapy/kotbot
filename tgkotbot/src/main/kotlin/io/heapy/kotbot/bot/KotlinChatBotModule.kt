package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.commands.CommandsModule
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.bot.filters.FiltersModule
import io.heapy.kotbot.bot.rules.RulesModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.metrics.MetricsModule

@Module
open class KotlinChatBotModule(
    private val daoModule: DaoModule,
    private val rulesModule: RulesModule,
    private val commandsModule: CommandsModule,
    private val filtersModule: FiltersModule,
    private val metricsModule: MetricsModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val kotbotModule: KotbotModule,
) {
    open val kotlinChatsBot: KotlinChatsBot by lazy {
        KotlinChatsBot(
            kotbot = kotbotModule.kotbot,
            filter = filtersModule.filter,
            meterRegistry = metricsModule.meterRegistry,
            updateDao = daoModule.updateDao,
            applicationScope = applicationScopeModule.applicationScope,
            commandResolver = commandsModule.commandResolver,
            ruleExecutor = rulesModule.ruleExecutor,
        )
    }
}
