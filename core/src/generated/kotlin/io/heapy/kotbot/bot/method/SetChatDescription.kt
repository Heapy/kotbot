package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns *True* on success.
 */
@Serializable
public data class SetChatDescription(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * New chat description, 0-255 characters
     */
    public val description: String? = null,
) : Method<Boolean> {
    public override suspend fun Kotbot.execute(): Boolean = requestForJson(
        name = "setChatDescription",
        serialize = {
            json.encodeToString(
                serializer(),
                this@SetChatDescription
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())
    }
}
