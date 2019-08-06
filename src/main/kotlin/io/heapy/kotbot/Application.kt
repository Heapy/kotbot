package io.heapy.kotbot

import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.rule.*
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.startServer
import io.heapy.logging.logger

/**
 * Entry point of bot.
 *
 * @author Ruslan Ibragimov
 */
object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val classLoader = Application::class.java.classLoader

        val configuration = Configuration()
        val metricsRegistry = createPrometheusMeterRegistry(configuration)
        val store = InMemoryStore()
        val state = State()
        val rules = listOfNotNull(
            deleteJoinRule,
            deleteSpamRule,
            defaultDeleteHelloRule,
            classLoader.getResource("contains.txt")?.let { resourceDeleteSwearingRule(it) },

            getIdRule(state),
            admRule(store, state),
            familyRules(store, state)
        )

        startServer(
            metricsRegistry
        )

        startBot(
            configuration,
            rules,
            state
        )

        LOGGER.info("Application started.")
    }

    private val LOGGER = logger<Application>()
}
