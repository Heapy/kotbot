package io.heapy.kotbot.web

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ContentNegotiation
import io.ktor.util.pipeline.ContextDsl

@ContextDsl
fun Application.JSON() {
    install(ContentNegotiation) {
        json()
    }
}
