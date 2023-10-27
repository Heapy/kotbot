package io.heapy.kotbot.web.routes

import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.util.KtorDsl
import kotlinx.serialization.Serializable
import javax.sql.DataSource

@KtorDsl
fun Routing.health(check: HealthCheck) {
    get("/api/health") {
        when (val response = check.doCheck()) {
            is Ok -> call.respond(HttpStatusCode.OK, response)
            is Neok -> call.respond(HttpStatusCode.ServiceUnavailable, response)
        }
    }
}

class CombinedHealthCheck(
    private val healthChecks: List<HealthCheck>,
) : HealthCheck {
    override fun doCheck(): HealthResponse {
        val responses = healthChecks.map {
            try {
                it.doCheck()
            } catch (e: Exception) {
                Neok(message = "Health check failed: ${e.message}")
            }
        }
        return if (responses.all { it is Ok }) {
            Ok()
        } else {
            Neok(
                message = responses
                    .filterIsInstance<Neok>()
                    .joinToString(separator = "\n") { it.message }
            )
        }
    }

}

interface HealthCheck {
    fun doCheck(): HealthResponse
}

class PingHealthCheck : HealthCheck {
    override fun doCheck(): HealthResponse {
        return Ok()
    }
}

class DatabaseHealthCheck(
    private val dataSource: DataSource,
) : HealthCheck {
    override fun doCheck(): HealthResponse {
        return dataSource.connection.use {
            if (it.isValid(5)) {
                Ok()
            } else {
                Neok(message = "Database is not available")
            }
        }
    }
}

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
data class Neok(
    override val status: String = "neok",
    override val message: String,
) : HealthResponse
