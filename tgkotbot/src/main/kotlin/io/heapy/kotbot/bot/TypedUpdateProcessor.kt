package io.heapy.kotbot.bot

interface TypedUpdateProcessor {
    suspend fun processUpdate(update: TypedUpdate)
}
