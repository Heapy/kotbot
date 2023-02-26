package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatAdministratorRights
import kotlin.Boolean
import kotlin.String
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
) : Method<GetMyDefaultAdministratorRights, ChatAdministratorRights> by Companion {
    public companion object : Method<GetMyDefaultAdministratorRights, ChatAdministratorRights> {
        override val _deserializer: KSerializer<Response<ChatAdministratorRights>> =
                Response.serializer(ChatAdministratorRights.serializer())

        override val _serializer: KSerializer<GetMyDefaultAdministratorRights> = serializer()

        override val _name: String = "getMyDefaultAdministratorRights"
    }
}
