package io.heapy.kotbot.bot

/**
 * Represents [BotStore] backed by in-memory structure. It is not persistent.
 */
class InMemoryStore : BotStore {
    override val families: MutableList<Family> = mutableListOf()
}
