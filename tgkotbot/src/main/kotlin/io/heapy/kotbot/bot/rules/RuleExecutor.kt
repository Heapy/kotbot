package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.micrometer.core.instrument.MeterRegistry

class RuleExecutor(
    private val rules: List<Rule>,
    private val meterRegistry: MeterRegistry,
    private val kotbot: Kotbot,
) {
    context(_: TransactionContext)
    suspend fun executeRules(update: TypedUpdate) {
        val actions = Actions(meterRegistry)
        return rules.forEach { rule ->
            rule.validate(kotbot, update, actions)
        }
    }
}
