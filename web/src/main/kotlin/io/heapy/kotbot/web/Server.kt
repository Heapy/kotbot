package io.heapy.kotbot.web

import io.heapy.komodo.shutdown.ShutdownManager
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

interface Server {
    fun start()
}

class KtorServer(
    private val metricsScrapper: () -> String,
    private val shutdownManager: ShutdownManager
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

        shutdownManager.addShutdownListener("KtorServer", 1) {
            server.stop(1000, 2000)
        }
    }
}
