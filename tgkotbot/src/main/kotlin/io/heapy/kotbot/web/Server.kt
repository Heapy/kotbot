package io.heapy.kotbot.web

import io.heapy.kotbot.web.routes.HealthCheck
import io.heapy.kotbot.web.routes.events
import io.heapy.kotbot.web.routes.health
import io.heapy.kotbot.web.routes.metrics
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing

interface Server {
    fun start()
}

class KtorServer(
    private val metricsScrapper: () -> String,
    private val healthCheck: HealthCheck,
) : Server {
    override fun start() {
        val server = embeddedServer(CIO, port = 8080) {
            JSON()

            routing {
                events()
                health(healthCheck)
                metrics(metricsScrapper)
            }
        }

        server.start(false)
    }
}
