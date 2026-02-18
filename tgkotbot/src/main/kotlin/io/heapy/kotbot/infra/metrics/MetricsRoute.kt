package io.heapy.kotbot.infra.metrics

import io.heapy.kotbot.infra.web.KtorRoute
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

class MetricsRoute(
    private val prometheusMeterRegistry: PrometheusMeterRegistry,
) : KtorRoute {
    override fun Routing.install() {
        get("/metrics") {
            call.respondText(text = prometheusMeterRegistry.scrape(), contentType = ContentType004)
        }
    }
}

internal val ContentType004 = ContentType(
    "text",
    "plain",
    parameters = listOf(
        HeaderValueParam("version", "0.0.4"),
        HeaderValueParam("charset", "utf-8")
    )
)
