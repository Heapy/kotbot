package io.heapy.kotbot.web

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.pipeline.ContextDsl

/**
 * Route for health checking
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
@ContextDsl
fun Routing.health() {
    get("/api/health") {
        call.respond(OK)
    }
}

internal val OK = HealthResponse("ok")

internal data class HealthResponse(val status: String)
