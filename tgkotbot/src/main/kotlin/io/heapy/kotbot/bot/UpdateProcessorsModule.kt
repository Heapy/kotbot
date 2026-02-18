package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.commands.CommandsModule
import io.heapy.kotbot.bot.filters.FiltersModule
import io.heapy.kotbot.bot.rules.RulesModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.metrics.MetricsModule

@Module
open class UpdateProcessorsModule(
    private val rulesModule: RulesModule,
    private val commandsModule: CommandsModule,
    private val filtersModule: FiltersModule,
    private val metricsModule: MetricsModule,
    private val callbackQueryProcessorModule: CallbackQueryProcessorModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
) {
    open val updateProcessor: UpdateProcessor by lazy {
        ParallelUpdateProcessor(
            typedUpdateProcessor = kotbotUpdateProcessor,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }

    open val kotbotUpdateProcessor: TypedUpdateProcessor by lazy {
        KotbotUpdateProcessor(
            filter = filtersModule.filter,
            meterRegistry = metricsModule.meterRegistry,
            commandResolver = commandsModule.commandResolver,
            ruleExecutor = rulesModule.ruleExecutor,
            callbackQueryProcessor = callbackQueryProcessorModule.callbackQueryProcessor,
            transactionProvider = jdbcModule.transactionProvider,
        )
    }
}
