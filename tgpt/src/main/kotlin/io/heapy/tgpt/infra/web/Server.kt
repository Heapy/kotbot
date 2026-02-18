package io.heapy.tgpt.infra.web

import io.heapy.tgpt.infra.withEach
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing

interface Server {
    fun start()
}

class KtorServer(
    private val routes: List<KtorRoute>,
) : Server {
    override fun start() {
        val server = embeddedServer(CIO, port = 8080) {
            JSON()

            routing {
                routes.withEach {
                    install()
                }
            }
        }

        server.start(false)
    }
}
