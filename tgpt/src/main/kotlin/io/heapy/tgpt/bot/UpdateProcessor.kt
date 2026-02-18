package io.heapy.tgpt.bot

import io.heapy.kotbot.bot.model.Update

interface UpdateProcessor {
    fun start()
    suspend fun processUpdate(update: Update)
}
