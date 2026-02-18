package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.anyMessage
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.infra.jdbc.TransactionContext

class DeletePropagandaRule : Rule {
    context(_: TransactionContext)
    override suspend fun validate(
        kotbot: Kotbot,
        update: TypedUpdate,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            val hasOffensiveText = listOfNotNull(
                message.from?.first_name,
                message.from?.last_name,
                message.text
            ).any {
                it.contains("🇷🇺")
            }

            if (hasOffensiveText) {
                log.info("Delete flag-message from ${message.from?.refLog}.")

                actions.runIfNew("zombie_rule", message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }

    private companion object : Logger()
}
