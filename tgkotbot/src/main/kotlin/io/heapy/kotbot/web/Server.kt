package io.heapy.kotbot.web

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing

interface Server {
    fun start()
}

class KtorServer(
    private val metricsScrapper: () -> String,
) : Server {
    override fun start() {
        val server = embeddedServer(CIO, port = 8080) {
            JSON()

            routing {
                health()
                metrics(metricsScrapper)
            }
        }

        server.start(false)
    }
}
