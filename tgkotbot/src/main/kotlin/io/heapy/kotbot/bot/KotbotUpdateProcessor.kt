package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.commands.CommandResolver
import io.heapy.kotbot.bot.filters.Filter
import io.heapy.kotbot.bot.rules.RuleExecutor
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.micrometer.core.instrument.MeterRegistry

class KotbotUpdateProcessor(
    meterRegistry: MeterRegistry,
    private val filter: Filter,
    private val commandResolver: CommandResolver,
    private val ruleExecutor: RuleExecutor,
    private val callbackQueryProcessor: CallbackQueryProcessor,
    private val transactionProvider: TransactionProvider,
) : TypedUpdateProcessor {
    private val updatesReceived = meterRegistry.counter("update.received")
    private val updatesFiltered = meterRegistry.counter("update.filtered")

    override suspend fun processUpdate(
        update: TypedUpdate,
    ) = transactionProvider.transaction {
        updatesReceived.increment()

        filter
            .predicate(update)
            .also { passed ->
                if (!passed) {
                    updatesFiltered.increment()
                }
            }

        when (update) {
            is TypedMessage -> {
                val result = commandResolver.findAndExecuteCommand(update.value)
                if (!result) {
                    ruleExecutor.executeRules(update)
                }
            }

            is TypedCallbackQuery -> {
                update.value.let { callbackQuery ->
                    callbackQueryProcessor.processCallbackQuery(callbackQuery)
                }
            }

            else -> log.info("Update type not handled: {}", update::class.simpleName)
        }
    }

    private companion object : Logger()
}
