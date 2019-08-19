package io.heapy.kotbot.bot

interface BotStore {
    val families: MutableList<Family>
}

class Family(
    val chatIds: MutableList<Long>,
    val adminChatId: Long
)
