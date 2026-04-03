package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to revoke the current token of a managed bot and generate a new one. Returns the new token as *String* on success.
 */
@Serializable
public data class ReplaceManagedBotToken(
    /**
     * User identifier of the managed bot whose token will be replaced
     */
    public val user_id: Long,
) : Method<ReplaceManagedBotToken, String> by Companion {
    public companion object : Method<ReplaceManagedBotToken, String> {
        override val _deserializer: KSerializer<Response<String>> =
                Response.serializer(String.serializer())

        override val _serializer: KSerializer<ReplaceManagedBotToken> = serializer()

        override val _name: String = "replaceManagedBotToken"
    }
}
