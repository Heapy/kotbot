package io.heapy.kotbot.web

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.util.pipeline.ContextDsl

/**
 * Enable JSON content negotiation.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
@ContextDsl
fun Application.JSON() {
    install(ContentNegotiation) {
        jackson {
        }
    }
}
