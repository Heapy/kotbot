package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns *True* on success.
 */
@Serializable
public data class SetChatAdministratorCustomTitle(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * New custom title for the administrator; 0-16 characters, emoji are not allowed
     */
    public val custom_title: String,
) : Method<Boolean> {
    public override suspend fun Kotbot.execute(): Boolean = requestForJson(
        name = "setChatAdministratorCustomTitle",
        serialize = {
            json.encodeToString(
                serializer(),
                this@SetChatAdministratorCustomTitle
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
