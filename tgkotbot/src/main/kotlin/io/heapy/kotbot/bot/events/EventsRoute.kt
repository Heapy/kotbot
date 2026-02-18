package io.heapy.kotbot.bot.events

import io.heapy.kotbot.infra.web.KtorRoute
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

class EventsRoute : KtorRoute {
    override fun Routing.install() {
        get("/api/events") {
            call.respond("There are will be events")
        }
    }
}
