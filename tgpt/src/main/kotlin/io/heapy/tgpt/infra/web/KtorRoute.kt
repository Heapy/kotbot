package io.heapy.tgpt.infra.web

import io.ktor.server.routing.Routing

interface KtorRoute {
    fun Routing.install()
}
