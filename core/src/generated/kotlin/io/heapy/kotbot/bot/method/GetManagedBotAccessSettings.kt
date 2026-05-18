package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotAccessSettings
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the access settings of a managed bot. Returns a [BotAccessSettings](https://core.telegram.org/bots/api/#botaccesssettings) object on success.
 */
@Serializable
public data class GetManagedBotAccessSettings(
    /**
     * User identifier of the managed bot whose access settings will be returned
     */
    public val user_id: Long,
) : Method<GetManagedBotAccessSettings, BotAccessSettings> by Companion {
    public companion object : Method<GetManagedBotAccessSettings, BotAccessSettings> {
        override val _deserializer: KSerializer<Response<BotAccessSettings>> =
                Response.serializer(BotAccessSettings.serializer())

        override val _serializer: KSerializer<GetManagedBotAccessSettings> = serializer()

        override val _name: String = "getManagedBotAccessSettings"
    }
}
