package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.infra.jdbc.TransactionContext

interface Rule {
    context(_: TransactionContext)
    suspend fun validate(
        kotbot: Kotbot,
        update: TypedUpdate,
        actions: Actions,
    )
}
