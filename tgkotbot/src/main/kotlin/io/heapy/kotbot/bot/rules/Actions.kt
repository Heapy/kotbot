package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Method
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags

class Actions(
    private val meterRegistry: MeterRegistry,
) {
    private val actions = mutableSetOf<Method<*, *>>()

    context(Rule)
    suspend fun <Request : Method<Request, Result>, Result> runIfNew(
        name: String,
        action: Request,
        block: suspend (Request) -> Unit,
    ) {
        if (action !in actions) {
            actions += action
            try {
                block(action)
                meterRegistry.counter(
                    "rule.trigger",
                    Tags.of("rule", name)
                ).increment()
            } catch (e: Exception) {
                meterRegistry.counter(
                    "rule.error",
                    Tags.of("rule", name)
                ).increment()
                throw e
            }
        }
    }
}
