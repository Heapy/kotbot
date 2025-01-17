package io.heapy.kotbot.infra.http_client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*

class HttpRequestLogger(
    private val config: Config,
) {
    class Config {
        var saveFunction: suspend (HttpResponse) -> Unit = {}
    }

    companion object Plugin : HttpClientPlugin<Config, HttpRequestLogger> {
        override val key = AttributeKey<HttpRequestLogger>("HttpRequestLogger")

        override fun prepare(block: Config.() -> Unit): HttpRequestLogger {
            val config = Config().apply(block)
            return HttpRequestLogger(config)
        }

        override fun install(plugin: HttpRequestLogger, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                proceed()
            }

            scope.receivePipeline.intercept(HttpReceivePipeline.After) { response ->
                plugin.config.saveFunction(response)
                proceedWith(response)
            }
        }
    }
}
