package io.heapy.kotbot.infra

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.http_client.HttpRequestLogger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Module
open class HttpClientModule {
    open val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(HttpRequestLogger) {
                // TODO: Provide service to store in DB
            }

            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}
