package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * This object contains information about the creation or token update of a bot that is managed by the current bot.
 */
@Serializable
public data class ManagedBotUpdated(
    /**
     * User that created the bot
     */
    public val user: User,
    /**
     * Information about the bot. Token of the bot can be fetched using the method [getManagedBotToken](https://core.telegram.org/bots/api/#getmanagedbottoken).
     */
    public val bot: User,
)
