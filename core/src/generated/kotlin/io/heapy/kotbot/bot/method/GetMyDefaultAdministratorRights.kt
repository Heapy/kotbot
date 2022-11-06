package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatAdministratorRights
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current default administrator rights of the bot. Returns [ChatAdministratorRights](https://core.telegram.org/bots/api/#chatadministratorrights) on success.
 */
@Serializable
public data class GetMyDefaultAdministratorRights(
    /**
     * Pass *True* to get default administrator rights of the bot in channels. Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
     */
    public val for_channels: Boolean? = null,
) : Method<ChatAdministratorRights> {
    public override suspend fun Kotbot.execute(): ChatAdministratorRights = requestForJson(
        name = "getMyDefaultAdministratorRights",
        serialize = {
            json.encodeToString(
                serializer(),
                this@GetMyDefaultAdministratorRights
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<ChatAdministratorRights>> =
                Response.serializer(ChatAdministratorRights.serializer())
    }
}
