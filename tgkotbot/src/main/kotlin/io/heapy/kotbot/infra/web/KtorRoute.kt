package io.heapy.kotbot.infra.web

import io.ktor.server.routing.*

interface KtorRoute {
    fun Routing.install()
}
