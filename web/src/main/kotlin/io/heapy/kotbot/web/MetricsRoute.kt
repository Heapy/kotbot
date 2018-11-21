package io.heapy.kotbot.web

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.pipeline.ContextDsl
import io.micrometer.prometheus.PrometheusMeterRegistry

/**
 * Expose metrics for prometheus.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
@ContextDsl
fun Routing.metrics(registry: PrometheusMeterRegistry) {
    get("/api/metrics") {
        call.respondText(text = registry.scrape(), contentType = ContentType004)
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
