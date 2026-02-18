package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.commands.CommandsModule
import io.heapy.kotbot.bot.commands.GptReplyHandler
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.bot.filters.FiltersModule
import io.heapy.kotbot.bot.rules.RulesModule
import io.heapy.kotbot.bot.use_case.callback.CallbackDataServiceModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.markdown.MarkdownModule
import io.heapy.kotbot.infra.metrics.MetricsModule
import io.heapy.kotbot.infra.openai.GptApiModule

@Module
open class UpdateProcessorsModule(
    private val rulesModule: RulesModule,
    private val commandsModule: CommandsModule,
    private val filtersModule: FiltersModule,
    private val metricsModule: MetricsModule,
    private val callbackQueryProcessorModule: CallbackQueryProcessorModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val daoModule: DaoModule,
    private val kotbotModule: KotbotModule,
    private val gptApiModule: GptApiModule,
    private val markdownModule: MarkdownModule,
    private val callbackDataServiceModule: CallbackDataServiceModule,
) {
    open val updateProcessor: UpdateProcessor by lazy {
        ParallelUpdateProcessor(
            typedUpdateProcessor = kotbotUpdateProcessor,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }

    open val gptReplyHandler: GptReplyHandler by lazy {
        GptReplyHandler(
            kotbot = kotbotModule.kotbot,
            gptService = gptApiModule.gptService,
            markdown = markdownModule.markdown,
            callbackDataService = callbackDataServiceModule.callbackDataService,
            gptSessionDao = daoModule.gptSessionDao,
        )
    }

    open val kotbotUpdateProcessor: TypedUpdateProcessor by lazy {
        KotbotUpdateProcessor(
            filter = filtersModule.filter,
            meterRegistry = metricsModule.meterRegistry,
            commandResolver = commandsModule.commandResolver,
            gptReplyHandler = gptReplyHandler,
            ruleExecutor = rulesModule.ruleExecutor,
            callbackQueryProcessor = callbackQueryProcessorModule.callbackQueryProcessor,
            transactionProvider = jdbcModule.transactionProvider,
            userContextDao = daoModule.userContextDao,
        )
    }
}
