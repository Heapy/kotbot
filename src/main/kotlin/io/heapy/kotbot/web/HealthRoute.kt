package io.heapy.kotbot.web

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.util.pipeline.ContextDsl

@ContextDsl
fun Routing.health() {
    get("/api/health") {
        call.respond(OK)
    }
}

internal val OK = HealthResponse("ok")

internal data class HealthResponse(val status: String)
