package io.heapy.kotbot.bot

import com.fasterxml.jackson.databind.ObjectMapper
import io.heapy.komodo.logging.logger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.telegram.telegrambots.Constants
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.facilities.TelegramHttpClientBuilder
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.io.InvalidObjectException
import java.lang.reflect.InvocationTargetException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets

/**
https://github.com/elbekD/kt-telegram-bot
https://github.com/InsanusMokrassar/TelegramBotAPI
https://github.com/telegraf/telegraf

https://habr.com/ru/post/445072/
https://github.com/ruslanys/telegraff

https://github.com/lamba92/telegram-bot-kotlin-api
https://github.com/lamba92/telegrambots-ktx

https://github.com/atipugin/telegram-bot-ruby
https://github.com/eternnoir/pyTelegramBotAPI
https://github.com/php-telegram-bot/core
https://github.com/mullwar/telebot

https://github.com/tranql/telegram-bot-api-schema
https://github.com/ark0f/tg-bot-api
https://github.com/omarmiatello/telegram-api-generator

https://github.com/rust-lang-by/rust-bot
 */
public class KotlinBotSession(
    private val callback: LongPollingKotBot,
    private val token: String,
    private val options: DefaultBotOptions = DefaultBotOptions(),
    private val objectMapper: ObjectMapper = ObjectMapper(),
) : AutoCloseable {
    private val job = Job()

    public fun start() {
        val httpclient = TelegramHttpClientBuilder.build(options)
        var requestConfig = options.requestConfig
        if (requestConfig == null) {
            requestConfig = RequestConfig.copy(RequestConfig.custom().build())
                .setSocketTimeout(Constants.SOCKET_TIMEOUT)
                .setConnectTimeout(Constants.SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(Constants.SOCKET_TIMEOUT).build()
        }

        var lastReceivedUpdate = -1

        CoroutineScope(job + CoroutineName("Kotlin Bot Session")).launch {
            val request = GetUpdates
                .builder()
                .limit(options.getUpdatesLimit)
                .timeout(options.getUpdatesTimeout)
                .offset(lastReceivedUpdate + 1)
                .build()

            if (options.allowedUpdates != null) {
                request.allowedUpdates = options.allowedUpdates
            }

            val url = options.baseUrl + token + "/" + GetUpdates.PATH

            val httpPost = HttpPost(url).also { post ->
                post.addHeader("charset", StandardCharsets.UTF_8.name())
                post.config = requestConfig
                post.entity = StringEntity(
                    objectMapper.writeValueAsString(request),
                    ContentType.APPLICATION_JSON
                )
            }

            try {
                httpclient.execute(httpPost, options.httpContext).use { response ->
                    val responseContent = EntityUtils.toString(
                        response.entity,
                        StandardCharsets.UTF_8
                    )
                    if (response.statusLine.statusCode >= 500) {
                        LOGGER.warn(responseContent)
                    } else {
                        try {
                            println(responseContent)
//                            request.deserializeResponse(responseContent)
                        } catch (e: JSONException) {
                            LOGGER.error("Error deserializing update: $responseContent", e)
                        }
                    }
                }
            } catch (e: SocketException) {
                LOGGER.error(e.localizedMessage, e)
            } catch (e: InvalidObjectException) {
                LOGGER.error(e.localizedMessage, e)
            } catch (e: TelegramApiRequestException) {
                LOGGER.error(e.localizedMessage, e)
            } catch (e: SocketTimeoutException) {
                LOGGER.info(e.localizedMessage, e)
            } catch (e: InternalError) {
                // handle InternalError to workaround OpenJDK bug (resolved since 13.0)
                // https://bugs.openjdk.java.net/browse/JDK-8173620
                if (e.cause is InvocationTargetException) {
                    val cause = e.cause?.cause
                    LOGGER.error(cause!!.localizedMessage, cause)
                } else throw e
            }
        }
    }

    override fun close() {
        // TODO: stop process
    }

    private companion object {
        private val LOGGER = logger<KotlinBotSession>()
    }
}
