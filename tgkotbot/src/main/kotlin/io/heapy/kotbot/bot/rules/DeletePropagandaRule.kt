package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.model.Update

class DeletePropagandaRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
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
                log.info("Delete flag-message from ${message.from?.info}.")

                actions.runIfNew("zombie_rule", message.delete) {
                    kotbot.executeSafely(it)
                }
            }
        }
    }

    private companion object : Logger()
}
