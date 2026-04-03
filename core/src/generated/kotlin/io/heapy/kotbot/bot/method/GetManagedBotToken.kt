package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get the token of a managed bot. Returns the token as *String* on success.
 */
@Serializable
public data class GetManagedBotToken(
    /**
     * User identifier of the managed bot whose token will be returned
     */
    public val user_id: Long,
) : Method<GetManagedBotToken, String> by Companion {
    public companion object : Method<GetManagedBotToken, String> {
        override val _deserializer: KSerializer<Response<String>> =
                Response.serializer(String.serializer())

        override val _serializer: KSerializer<GetManagedBotToken> = serializer()

        override val _name: String = "getManagedBotToken"
    }
}
