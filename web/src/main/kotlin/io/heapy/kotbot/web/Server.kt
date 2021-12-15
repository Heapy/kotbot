package io.heapy.kotbot.web

import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

interface Server {
    fun start()
}

class KtorServer(
    private val metricsScrapper: () -> String,
) : Server {
    override fun start() {
        val server = embeddedServer(Netty, port = 8080) {
            JSON()

            routing {
                health()
                metrics(metricsScrapper)
            }
        }

        server.start(false)
    }
}
