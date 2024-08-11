package io.heapy.kotbot.infra.health_check

import kotlinx.serialization.Serializable

interface HealthCheck {
    fun doCheck(): HealthResponse

    sealed interface HealthResponse {
        val status: String
        val message: String
    }

    @Serializable
    data class Ok(
        override val status: String = "ok",
        override val message: String = "ok",
    ) : HealthResponse

    @Serializable
    data class Nok(
        override val status: String = "neok",
        override val message: String,
    ) : HealthResponse
}
