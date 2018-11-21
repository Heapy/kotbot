package io.heapy.kotbot.bot

/**
 * Configuration required for TG bot.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
interface BotConfiguration {
    val token: String
    val name: String
}
