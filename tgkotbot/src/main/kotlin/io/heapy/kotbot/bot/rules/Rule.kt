package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.dao.UserContext
import io.heapy.kotbot.infra.jdbc.TransactionContext

data class RuleContext(
    val userContext: UserContext? = null,
)

context(ruleContext: RuleContext)
val userContext: UserContext?
    get() = ruleContext.userContext

interface Rule {
    context(_: TransactionContext, _: RuleContext)
    suspend fun validate(
        kotbot: Kotbot,
        update: TypedUpdate,
        actions: Actions,
    )
}
