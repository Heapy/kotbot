package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.anyMessage
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.micrometer.core.instrument.MeterRegistry

class RuleExecutor(
    private val rules: List<Rule>,
    private val meterRegistry: MeterRegistry,
    private val kotbot: Kotbot,
    private val userContextDao: UserContextDao,
) {
    context(_: TransactionContext)
    suspend fun executeRules(update: TypedUpdate) {
        val actions = Actions(meterRegistry)
        val userId = update.anyMessage?.from?.id
        val userContext = userId?.let { userContextDao.get(it) }
        val ruleContext = RuleContext(userContext = userContext)
        context(ruleContext) {
            rules.forEach { rule ->
                rule.validate(kotbot, update, actions)
            }
        }
    }
}
