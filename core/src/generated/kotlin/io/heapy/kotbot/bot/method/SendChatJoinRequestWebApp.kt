package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to process a received chat join request query by showing a Mini App to the user before deciding the outcome. Returns *True* on success.
 */
@Serializable
public data class SendChatJoinRequestWebApp(
    /**
     * Unique identifier of the join request query
     */
    public val chat_join_request_query_id: String,
    /**
     * The URL of the Mini App to be opened
     */
    public val web_app_url: String,
) : Method<SendChatJoinRequestWebApp, Boolean> by Companion {
    public companion object : Method<SendChatJoinRequestWebApp, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SendChatJoinRequestWebApp> = serializer()

        override val _name: String = "sendChatJoinRequestWebApp"
    }
}
