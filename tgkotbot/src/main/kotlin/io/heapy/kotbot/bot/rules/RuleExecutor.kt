package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.model.Update
import io.micrometer.core.instrument.MeterRegistry

class RuleExecutor(
    private val rules: List<Rule>,
    private val meterRegistry: MeterRegistry,
    private val kotbot: Kotbot,
) {
    suspend fun executeRules(update: Update) {
        val actions = Actions(meterRegistry)
        return rules.forEach { rule ->
            rule.validate(kotbot, update, actions)
        }
    }
}
