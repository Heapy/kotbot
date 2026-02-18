package io.heapy.kotbot.infra.http_client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.util.AttributeKey

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
