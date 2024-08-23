package io.heapy.kotbot.bot.rules

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.model.Update

interface Rule {
    suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    )
}
