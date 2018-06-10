package link.kotlin.kotbot.configuration

import java.lang.System.getenv

/**
 * @author Ruslan Ibragimov
 */
interface KotbotConfiguration {
    val token: String
    val name: String
}

class DefaultKotbotConfiguration(
    override val token: String = getenv("KOTBOT_TOKEN"),
    override val name: String = "KotBot"
) : KotbotConfiguration
