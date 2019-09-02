package io.heapy.kotbot

import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.rule.*
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.startServer
import io.heapy.logging.logger
import kotlinx.coroutines.runBlocking

/**
 * Entry point of bot.
 *
 * @author Ruslan Ibragimov
 */
object Application {
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        val classLoader = Application::class.java.classLoader

        val configuration = Configuration()
        val metricsRegistry = createPrometheusMeterRegistry(configuration)
        val store = InMemoryStore()
        val state = State()
        val rules = listOfNotNull(
            policyRules(classLoader.getResource("contains.txt")),
            devRules(store, state),
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

        Unit
    }

    private val LOGGER = logger<Application>()
}
