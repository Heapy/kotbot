package io.heapy.kotbot.bot

class InMemoryStore : BotStore {
    override val families: MutableList<Family> = mutableListOf()
}
