package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Int
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get the number of members in a chat. Returns *Int* on success.
 */
@Serializable
public data class GetChatMemberCount(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
) : Method<Int> {
    public override suspend fun Kotbot.execute(): Int = requestForJson(
        name = "getChatMemberCount",
        serialize = {
            json.encodeToString(
                serializer(),
                this@GetChatMemberCount
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<Int>> = Response.serializer(Int.serializer())
    }
}
