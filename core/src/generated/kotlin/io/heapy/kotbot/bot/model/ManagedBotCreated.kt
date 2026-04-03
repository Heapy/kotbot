package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * This object contains information about the bot that was created to be managed by the current bot.
 */
@Serializable
public data class ManagedBotCreated(
    /**
     * Information about the bot. The bot's token can be fetched using the method [getManagedBotToken](https://core.telegram.org/bots/api/#getmanagedbottoken).
     */
    public val bot: User,
)
