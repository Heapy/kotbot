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
        val configuration = Configuration()
        val metricsRegistry = createPrometheusMeterRegistry(configuration)
        val store = InMemoryStore().apply {
            //families += Family(mutableListOf(-1001416698098L), -348604360L) // admin dialog: 50470510
        }
        val state = State()
        val rules = listOf(
            DeleteJoinRule(),
            DeleteSpamRule(),
            DeleteHelloRule(),
            DeleteSwearingRule(),

            GetIdRule(store),
            ReportRule(store),
            FamilyStartRule(state),
            FamilyLeaveRule(store, state),
            RefreshPermissionsCallbackRule(state),
            FamilyManageRule(store, state)
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
