package io.heapy.kotbot.web.routes

import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.util.KtorDsl

@KtorDsl
fun Routing.metrics(scrape: () -> String) {
    get("/metrics") {
        call.respondText(text = scrape(), contentType = ContentType004)
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
