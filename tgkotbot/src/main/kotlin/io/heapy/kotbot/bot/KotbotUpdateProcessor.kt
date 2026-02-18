package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.commands.CommandResolver
import io.heapy.kotbot.bot.commands.GptReplyHandler
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.bot.filters.Filter
import io.heapy.kotbot.bot.model.User
import io.heapy.kotbot.bot.rules.RuleExecutor
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.micrometer.core.instrument.MeterRegistry

class KotbotUpdateProcessor(
    meterRegistry: MeterRegistry,
    private val filter: Filter,
    private val commandResolver: CommandResolver,
    private val gptReplyHandler: GptReplyHandler,
    private val ruleExecutor: RuleExecutor,
    private val callbackQueryProcessor: CallbackQueryProcessor,
    private val transactionProvider: TransactionProvider,
    private val userContextDao: UserContextDao,
) : TypedUpdateProcessor {
    private val updatesReceived = meterRegistry.counter("update.received")
    private val updatesFiltered = meterRegistry.counter("update.filtered")

    override suspend fun processUpdate(
        update: TypedUpdate,
    ) = transactionProvider.transaction {
        updatesReceived.increment()

        extractUser(update)?.let { user ->
            val displayName = buildString {
                append(user.first_name)
                user.last_name?.let { append(" ").append(it) }
            }
            userContextDao.insertIfNotExists(user.id, displayName)
        }

        val passed = filter
            .predicate(update)
            .also { passed ->
                if (!passed) {
                    updatesFiltered.increment()
                }
            }

        if (passed) {
            when (update) {
                is TypedMessage -> {
                    val result = commandResolver.findAndExecuteCommand(update.value)
                    if (!result) {
                        val gptHandled = gptReplyHandler.handleIfGptReply(update.value)
                        if (!gptHandled) {
                            ruleExecutor.executeRules(update)
                        }
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
    }

    private fun extractUser(update: TypedUpdate): User? = when (update) {
        is TypedMessage -> update.value.from
        is TypedEditedMessage -> update.value.from
        is TypedChannelPost -> update.value.from
        is TypedEditedChannelPost -> update.value.from
        is TypedCallbackQuery -> update.value.from
        is TypedInlineQuery -> update.value.from
        is TypedChosenInlineResult -> update.value.from
        is TypedShippingQuery -> update.value.from
        is TypedPreCheckoutQuery -> update.value.from
        is TypedMyChatMember -> update.value.from
        is TypedChatMember -> update.value.from
        is TypedChatJoinRequest -> update.value.from
        is TypedBusinessMessage -> update.value.from
        is TypedEditedBusinessMessage -> update.value.from
        else -> null
    }

    private companion object : Logger()
}
