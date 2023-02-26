package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatAdministratorRights
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the default administrator rights requested by the bot when it's added as an administrator to groups or channels. These rights will be suggested to users, but they are free to modify the list before adding the bot. Returns *True* on success.
 */
@Serializable
public data class SetMyDefaultAdministratorRights(
    /**
     * A JSON-serialized object describing new default administrator rights. If not specified, the default administrator rights will be cleared.
     */
    public val rights: ChatAdministratorRights? = null,
    /**
     * Pass *True* to change the default administrator rights of the bot in channels. Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
     */
    public val for_channels: Boolean? = null,
) : Method<SetMyDefaultAdministratorRights, Boolean> by Companion {
    public companion object : Method<SetMyDefaultAdministratorRights, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMyDefaultAdministratorRights> = serializer()

        override val _name: String = "setMyDefaultAdministratorRights"
    }
}
