package io.heapy.kotbot.infra.health_check

import io.heapy.kotbot.infra.health_check.HealthCheck.Nok
import io.heapy.kotbot.infra.health_check.HealthCheck.Ok
import io.heapy.kotbot.infra.web.KtorRoute
import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

class HealthCheckRoute(
    private val healthCheck: HealthCheck,
) : KtorRoute {
    override fun Routing.install() {
        get("/healthcheck") {
            when (val response = healthCheck.doCheck()) {
                is Ok -> call.respond(HttpStatusCode.OK, response)
                is Nok -> call.respond(HttpStatusCode.ServiceUnavailable, response)
            }
        }
    }
}
