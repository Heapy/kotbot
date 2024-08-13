package io.heapy.kotbot.infra.http_client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*

class HttpRequestLogger(
    config: Config,
) {
    class Config {
        // Configuration options if needed
    }

    companion object Plugin : HttpClientPlugin<Config, HttpRequestLogger> {
        override val key = AttributeKey<HttpRequestLogger>("HttpRequestLogger")

        override fun prepare(block: Config.() -> Unit): HttpRequestLogger {
            val config = Config().apply(block)
            return HttpRequestLogger(config)
        }

        override fun install(plugin: HttpRequestLogger, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                println("Request: ${context.url}")
                proceed()
            }

            scope.receivePipeline.intercept(HttpReceivePipeline.After) { response ->
                println("Response: ${response.status.value}")
                proceedWith(response)
            }
        }
    }
}
