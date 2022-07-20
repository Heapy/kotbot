package io.heapy.kotbot.web.routes

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.events() {
    get("/api/events") {
        call.respond("There are will be events")
    }
}
