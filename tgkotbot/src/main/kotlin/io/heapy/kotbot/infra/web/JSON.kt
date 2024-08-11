package io.heapy.kotbot.infra.web

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.util.KtorDsl

@KtorDsl
fun Application.JSON() {
    install(ContentNegotiation) {
        json()
    }
}
